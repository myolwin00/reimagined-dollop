package com.myolwinoo.universalyoga.admin.features.course.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.launch

class CourseDetailViewModel(
    private val courseId: String,
    private val repo: YogaRepository
) : ViewModel() {

    // Exposes the details of the yoga course as a Flow, collecting from the repository.
    val course = repo.getCourseDetails(courseId)

    // Holds the ID of the class to be deleted, if any.
    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    fun deleteClass(classId: String) {
        viewModelScope.launch {
            repo.deleteClass(classId)
        }
    }

    class Factory(
        private val courseId: String,
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CourseDetailViewModel(
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}