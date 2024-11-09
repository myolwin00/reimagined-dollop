package com.myolwinoo.universalyoga.admin.features.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: YogaRepository
): ViewModel() {

    val courses: Flow<List<YogaCourse>> = repo.getAllCourses()

    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    var confirmUpload = mutableStateOf(false)
        private set

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

    fun uploadDataToServer() {

    }

    class Factory(
        private val repo: YogaRepository
    ):  ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return HomeViewModel(
                repo = repo
            ) as T
        }
    }
}

