package com.myolwinoo.universalyoga.admin.features.create

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.DifficultyLevel
import com.myolwinoo.universalyoga.admin.data.model.TargetAudience
import com.myolwinoo.universalyoga.admin.data.model.YogaClassType
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.util.UUID

class CreateCourseScreenViewModel(
    private val courseId: String? = null,
    private val repo: YogaRepository
) : ViewModel() {

    var classType = mutableStateOf(YogaClassType.FLOW_YOGA)
    var selectedDayOfWeek = mutableStateOf(DayOfWeek.MONDAY)
    var duration = mutableStateOf(TextFieldValue())
    var time = mutableStateOf(TextFieldValue())
    var capacity = mutableStateOf(TextFieldValue())
    var price = mutableStateOf(TextFieldValue())
    val description = mutableStateOf(TextFieldValue())
    var difficulty = mutableStateOf(DifficultyLevel.BEGINNER)
    var targetAudience = mutableStateOf(TargetAudience.ADULTS)
    var cancellationPolicy = mutableStateOf(CancellationPolicy.FLEXIBLE)

    val navigateToHome = mutableStateOf(false)
    val inputError = mutableStateOf<CourseInputError?>(null)

    init {
        viewModelScope.launch {
            courseId
                ?.let { repo.getCourse(it) }
                ?.let {
                    classType.value = it.typeOfClass
                    selectedDayOfWeek.value = it.dayOfWeek
                    duration.value = TextFieldValue(it.duration, TextRange(it.duration.length))
                    time.value = TextFieldValue(it.time, TextRange(it.time.length))
                    capacity.value = TextFieldValue(it.capacity.toString(), TextRange(it.capacity.toString().length))
                    price.value = TextFieldValue(it.pricePerClass.toString(), TextRange(it.pricePerClass.toString().length))
                    description.value = TextFieldValue(it.description, TextRange(it.description.length))
                    difficulty.value = it.difficultyLevel
                    targetAudience.value = it.targetAudience
                    cancellationPolicy.value = it.cancellationPolicy
                }
        }
    }

    fun create() {
        val error = validateInputs()
        if (error != null) {
            inputError.value = error
            return
        }

        viewModelScope.launch {
            val course = YogaCourse(
                id = courseId ?: UUID.randomUUID().toString(),

                dayOfWeek = selectedDayOfWeek.value,
                time = time.value.text.trim(),
                duration = duration.value.text.trim(),

                typeOfClass = classType.value,
                difficultyLevel = difficulty.value,
                targetAudience = targetAudience.value,
                description = description.value.text,

                capacity = capacity.value.text.toInt(),
                pricePerClass = price.value.text.toDouble(),
                cancellationPolicy =cancellationPolicy.value,

                classes = emptyList()
            )
            repo.createCourse(course)
                .onSuccess { navigateToHome.value = true }
        }
    }

    fun resetInputError() {
        inputError.value = null
    }

    private fun validateInputs(): CourseInputError? {
        if (time.value.text.trim().isBlank()) {
            return CourseInputError.StartTime
        }
        if (duration.value.text.trim().toIntOrNull() == null) {
            return CourseInputError.Duration
        }
        if (capacity.value.text.trim().toIntOrNull() == null) {
            return CourseInputError.Capacity
        }
        if (price.value.text.trim().toDoubleOrNull() == null) {
            return CourseInputError.Price
        }
        return null
    }

    class Factory(
        val courseId: String? = null,
        private val repo: YogaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CreateCourseScreenViewModel(
                courseId = courseId,
                repo = repo
            ) as T
        }
    }
}