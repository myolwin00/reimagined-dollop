package com.myolwinoo.universalyoga.admin.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel: ViewModel() {

    val courses: MutableStateFlow<List<YogaCourse>> =
        MutableStateFlow(DummyDataProvider.dummyYogaCourses)

    class Factory:  ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return HomeViewModel() as T
        }
    }
}

