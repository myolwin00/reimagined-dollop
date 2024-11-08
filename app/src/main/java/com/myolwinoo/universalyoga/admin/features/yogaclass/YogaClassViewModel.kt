package com.myolwinoo.universalyoga.admin.features.yogaclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.launch
import java.util.UUID

class YogaClassViewModel(
    private val courseId: String,
    private val repo: YogaRepository
) : ViewModel() {

    val course = repo.getCourse(courseId)

    fun createClass() {
        viewModelScope.launch {
            repo.createYogaClass(
                YogaClass(
                    id = UUID.randomUUID().toString(),
                    date = "08/11/2014",
                    courseId = courseId,
                    teacherId = UUID.randomUUID().toString(),
                    teacherName = "Tr.Phyu",
                    comment = "Sample comment.",
                )
            )
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
            return YogaClassViewModel(
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}