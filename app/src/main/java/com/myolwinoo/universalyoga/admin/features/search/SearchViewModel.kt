@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.myolwinoo.universalyoga.admin.features.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

private const val SEARCH_DEBOUNCE_MILLIS = 300L

class SearchViewModel(
    private val repo: YogaRepository
) : ViewModel() {

    private val allYogaClasses = repo.getAllYogaClasses()

    // Holds the current search query entered by the user.
    var searchQuery = mutableStateOf(TextFieldValue(""))
        private set
    private val searchQueryFlow = MutableStateFlow("")

    // Provides a flow of teacher name suggestions based on the search query.
    // Emits an empty list if the query is blank.
    val suggestions = searchQueryFlow
        .debounce(SEARCH_DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { if (it.isBlank()) flowOf(emptyList()) else repo.getTeacherNameSuggestions(it) }
        .map { it.map { (_, name) -> name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Holds the currently selected date filter, if any.
    var dateFilter by mutableStateOf<String?>(null)
        private set
    private val dateFilterFlow = MutableStateFlow<String?>(null)

    // Holds the currently selected day of the week filter, if any.
    var dayFilter by mutableStateOf<DayOfWeek?>(null)
        private set
    private val dayFilterFlow = MutableStateFlow<DayOfWeek?>(null)

    // Combines search query, all yoga classes, date filter, and day filter to produce search results.
    val searchResult: StateFlow<List<YogaClass>> = combine(
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .distinctUntilChanged(),
        allYogaClasses,
        dateFilterFlow,
        dayFilterFlow,
    ) { query, yogaClasses, dateFilter, dayFiler ->
        if (query.isBlank()) {
            emptyList()
        } else {
            yogaClasses.filter {
                val nameContains = it.teachers.any { teacherName ->
                    teacherName.contains(query, true)
                }
                val dateMatches = dateFilter == null || it.date == dateFilter
                val dayMatches = dayFiler == null || it.dayOfWeek == dayFiler
                nameContains && dateMatches && dayMatches
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Holds the ID of the yoga class to be deleted, if any.
    var confirmDeleteId = mutableStateOf<String?>(null)
        private set

    fun showConfirmDelete(id: String) {
        confirmDeleteId.value = id
    }

    fun hideConfirmDelete() {
        confirmDeleteId.value = null
    }

    // Updates the search query and triggers the search flow.
    fun updateQuery(query: TextFieldValue) {
        searchQuery.value = query
        searchQueryFlow.value = query.text
    }

    // Updates the date filter and triggers the search flow.
    fun updateDateFilter(date: String?) {
        dateFilter = date
        dateFilterFlow.value = date
    }

    // Updates the day filter and triggers the search flow.
    fun updateDayFilter(day: DayOfWeek?) {
        dayFilter = day
        dayFilterFlow.value = day
    }

    fun deleteYogaClass(classId: String) {
        viewModelScope.launch {
            repo.deleteClass(classId)
        }
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

