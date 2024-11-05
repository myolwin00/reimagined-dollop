package com.myolwinoo.universalyoga.admin.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val repo: YogaRepository
): ViewModel() {

    val courses: Flow<List<YogaCourse>> = repo.getAllCourses()

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

