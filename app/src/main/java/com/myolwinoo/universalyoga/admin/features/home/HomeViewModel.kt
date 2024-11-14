package com.myolwinoo.universalyoga.admin.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.usecase.SyncDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: YogaRepository,
    private val syncDataUseCase: SyncDataUseCase,
) : ViewModel() {

    // Exposes the list of yoga courses as a StateFlow, collecting from the repository
    // and emitting updates to the UI.
    val courses: StateFlow<List<YogaCourse>> = repo.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Holds the ID of the course to be deleted, if any.
    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    // Indicates whether the upload confirmation dialog should be shown.
    var confirmUpload = mutableStateOf(false)
        private set

    // Holds any error that occurred during data upload.
    var uploadError by mutableStateOf<Throwable?>(null)

    // Indicates whether the data upload was successful.
    var uploadSuccess by mutableStateOf(false)

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    fun showConfirmUpload() {
        confirmUpload.value = true
    }

    fun hideConfirmUpload() {
        confirmUpload.value = false
    }

    fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            repo.deleteCourse(courseId)
        }
    }

    // Uploads data to the server using the SyncDataUseCase.
    // If skipEvents is true, success/error events are not emitted.
    fun uploadDataToServer(skipEvents: Boolean = false) {
        viewModelScope.launch {
            syncDataUseCase.uploadToFireStore(courses.value, true)
                .onSuccess {
                    if (!skipEvents) {
                        uploadSuccess = true
                    }
                }
                .onFailure { e ->
                    if (!skipEvents) {
                        uploadError = e
                    }
                }
        }
    }

    class Factory(
        private val repo: YogaRepository,
        private val syncDataUseCase: SyncDataUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return HomeViewModel(
                repo = repo,
                syncDataUseCase = syncDataUseCase,
            ) as T
        }
    }
}

