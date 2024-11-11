package com.myolwinoo.universalyoga.admin.usecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

class SyncDataUseCase(
    private val repo: YogaRepository
) {
    private val collection = Firebase.firestore.collection("yoga_courses")

    suspend fun start() {
        repo.getAllCourses()
            .onEach { uploadToFireStore(it, false) }
            .collect()
    }

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