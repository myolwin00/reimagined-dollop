@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.myolwinoo.universalyoga.admin.features.yogaclass.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.util.UUID

private const val DEBOUNCE_MILLIS = 300L

class CreateYogaClassViewModel(
    private val classId: String?,
    private val courseId: String,
    private val repo: YogaRepository
): ViewModel() {

    var teacherName by mutableStateOf(TextFieldValue())
        private set
    private val teacherNameFlow = MutableStateFlow("")

    var teacherList by mutableStateOf<List<String>>(emptyList())
        private set

    var date by mutableStateOf(TextFieldValue())
    var comment by mutableStateOf(TextFieldValue())

    var dayOfWeek by mutableStateOf(DayOfWeek.MONDAY)
        private set

    // Indicates whether the screen should navigate back to the previous screen.
    var navigateBack by mutableStateOf(false)
    // Holds any input error that occurred during class creation.
    var inputError by mutableStateOf<ClassInputError?>(null)

    // Provides a flow of teacher name suggestions based on the entered teacher name.
    // Emits an empty list if the entered name is blank.
    private val _suggestions = teacherNameFlow
        .debounce(DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { if (it.isBlank()) flowOf(emptyList()) else repo.getTeacherNameSuggestions(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // Exposes the teacher name suggestions as a StateFlow.
    val suggestions = _suggestions
        .map { it.map { (_, name) -> name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        viewModelScope.launch {
            // Fetch the day of the week from the course and update the state.
            repo.getCourse(courseId)?.dayOfWeek
                ?.also { dayOfWeek = it }

            // If classId is provided, fetch and populate class details for editing.
            classId
                ?.let { repo.getYogaClass(it) }
                ?.also {
                    teacherList = it.teachers
                    date = TextFieldValue(it.date, TextRange(it.date.length))
                    comment = TextFieldValue(it.comment, TextRange(it.comment.length))
                }
        }
    }

    fun updateTeacherName(query: TextFieldValue) {
        teacherName = query
        teacherNameFlow.value = query.text
        resetInputError()
    }

    fun addTeacher() {
        teacherList = teacherList + teacherName.text.trim()
        updateTeacherName(TextFieldValue())
    }

    fun removeTeacher(name: String) {
        teacherList = teacherList.filter { it != name }
    }

    fun resetInputError() {
        inputError = null
    }

    // Creates or updates the yoga class.
    fun createClass() {
        val error = validateInputs()
        if (error != null) {
            inputError = error
            return
        }

        viewModelScope.launch {
            repo.createYogaClass(
                YogaClass(
                    id = classId ?: UUID.randomUUID().toString(),
                    courseId = courseId,
                    teachers = teacherList,
                    date = date.text,
                    comment = comment.text.trim(),
                )
            ).onSuccess {
                navigateBack = true
            }
        }
    }

    // Validates class inputs and returns an error if any are invalid.
    private fun validateInputs(): ClassInputError? {
        if (teacherList.isEmpty()) {
            return ClassInputError.TeacherName
        }
        if (date.text.trim().isBlank()) {
            return ClassInputError.Date
        }
        return null
    }

    class Factory(
        private val classId: String?,
        private val courseId: String,
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CreateYogaClassViewModel(
                classId = classId,
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}