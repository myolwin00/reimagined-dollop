package com.myolwinoo.universalyoga.admin.features.create

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.DayOfWeek
import com.myolwinoo.universalyoga.admin.data.model.DifficultyLevel
import com.myolwinoo.universalyoga.admin.data.model.TargetAudience
import com.myolwinoo.universalyoga.admin.data.model.YogaClassType
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import kotlinx.coroutines.launch
import java.util.UUID

class CreateCourseScreenViewModel(
    private val repo: YogaRepository
) : ViewModel() {

    var classType = mutableStateOf(YogaClassType.FLOW_YOGA)
    var selectedDayOfWeek = mutableStateOf(DayOfWeek.MONDAY)
    var duration = mutableStateOf(TextFieldValue())
    var time = mutableStateOf(TextFieldValue())
    var capacity = mutableStateOf(TextFieldValue())
    var price = mutableStateOf(TextFieldValue())
    var description = mutableStateOf(TextFieldValue())
    var difficulty = mutableStateOf(DifficultyLevel.BEGINNER)
    var targetAudience = mutableStateOf(TargetAudience.ADULTS)
    var cancellationPolicy = mutableStateOf(CancellationPolicy.FLEXIBLE)

    var navigateToHome = mutableStateOf(false)

    fun create() {
        viewModelScope.launch {
            val course = YogaCourse(
                id = UUID.randomUUID().toString(),
                dayOfWeek = selectedDayOfWeek.value,
                time = time.value.text,
                capacity = capacity.value.text.toInt(),
                duration = duration.value.text,
                pricePerClass = price.value.text.toDouble(),
                typeOfClass = classType.value,
                description = description.value.text,
                difficultyLevel = difficulty.value,
                cancellationPolicy =cancellationPolicy.value,
                targetAudience = targetAudience.value,
                classes = emptyList()
            )
            repo.createCourse(course)
                .onSuccess { navigateToHome.value = true }
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
            return CreateCourseScreenViewModel(
                repo = repo
            ) as T
        }
    }
}