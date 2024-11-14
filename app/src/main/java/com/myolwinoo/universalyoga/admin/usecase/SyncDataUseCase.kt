package com.myolwinoo.universalyoga.admin.usecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

/**
 * Use case for synchronizing data between the local database and Firebase Firestore.
 */
class SyncDataUseCase(
    private val repo: YogaRepository
) {
    private val collection = Firebase.firestore.collection("yoga_courses")

    /**
     * Starts the data synchronization process.
     *
     * This method retrieves all yoga courses from the local database and uploads them to Firestore.
     * It also listens for changes in the local database and updates Firestore accordingly.
     */
    suspend fun start() {
        repo.getAllCourses()
            .onEach { uploadToFireStore(it, false) }
            .collect()
    }

    /**
     * Uploads a list of yoga courses to Firestore.
     *
     * @param courses The list of yoga courses to upload.
     * @param reset If true, all existing documents in the Firestore collection will be deleted before uploading the new courses.
     * @return A [Result] indicating success or failure of the upload operation.
     */
    suspend fun uploadToFireStore(
        courses: List<YogaCourse>,
        reset: Boolean
    ): Result<Unit> {
        return try {
            if (reset) {
                // clear all document from firestore
                val result = collection.get().await()
                result.documents.forEach { doc ->
                    doc.reference.delete()
                }
            }
            // save all courses to firestore
            courses.forEach {
                // ignore bitmap while uploading to firestore
                val course = it.copy(
                    images = it.images.map { image ->
                        image.copy(bitmap = null)
                    }
                )
                collection.document(it.id)
                    .set(course)
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}