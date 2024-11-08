@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.myolwinoo.universalyoga.admin.features.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

private const val SEARCH_DEBOUNCE_MILLIS = 300L

class SearchViewModel(
    private val repo: YogaRepository
) : ViewModel() {

    var searchQuery = mutableStateOf(TextFieldValue(""))
        private set

    private val allCourses = repo.getAllCourses()
    private val searchQueryFlow = MutableStateFlow("")

    val suggestions = searchQueryFlow
        .debounce(SEARCH_DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { if (it.isBlank()) flowOf(emptyList()) else repo.getTeacherNameSuggestions(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val searchResult: StateFlow<List<YogaCourse>> = combine(
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .distinctUntilChanged(),
        allCourses
    ) { query, courses ->
        Log.i("SearchViewModel", "query: $query")
        if (query.isBlank()) {
            emptyList()
        } else {
            courses.filter {
                it.classes.all { yogaClass ->
                    yogaClass.teacherName.contains(query, true)
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun updateQuery(query: TextFieldValue) {
        searchQuery.value = query
        searchQueryFlow.value = query.text
    }

    class Factory(
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return SearchViewModel(
                repo = repo
            ) as T
        }
    }
}

