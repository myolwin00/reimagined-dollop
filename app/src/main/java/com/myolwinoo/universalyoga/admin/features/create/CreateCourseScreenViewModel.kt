package com.myolwinoo.universalyoga.admin.features.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.myolwinoo.universalyoga.admin.data.model.YogaEventType
import com.myolwinoo.universalyoga.admin.data.model.YogaImage
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.utils.ImageUtils
import com.myolwinoo.universalyoga.admin.utils.LocationHelper
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.util.UUID

class CreateCourseScreenViewModel(
    private val courseId: String? = null,
    private val repo: YogaRepository,
    private val imageUtils: ImageUtils,
    private val locationHelper: LocationHelper,
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
    var images by mutableStateOf(emptyList<YogaImage>())
        private set
    var eventType by mutableStateOf(YogaEventType.UNSPECIFIED)
        private set
    var latitude by mutableStateOf(TextFieldValue())
    var longitude by mutableStateOf(TextFieldValue())
    var url by mutableStateOf(TextFieldValue())

    // Indicates whether it should navigate back to home screen.
    val navigateToHome = mutableStateOf(false)
    // Holds any input error that occurred during course creation.
    val inputError = mutableStateOf<CourseInputError?>(null)
    // Indicates whether the save confirmation dialog should be shown.
    var showConfirmSave by mutableStateOf(false)

    init {
        // If courseId is provided, fetch and populate course details for editing.
        viewModelScope.launch {
            courseId
                ?.let { repo.getCourse(it) }
                ?.let {
                    classType.value = it.typeOfClass
                    selectedDayOfWeek.value = it.dayOfWeek
                    duration.value = TextFieldValue(it.duration, TextRange(it.duration.length))
                    time.value = TextFieldValue(it.time, TextRange(it.time.length))
                    capacity.value = TextFieldValue(
                        it.capacity.toString(),
                        TextRange(it.capacity.toString().length)
                    )
                    price.value = TextFieldValue(
                        it.pricePerClass.toString(),
                        TextRange(it.pricePerClass.toString().length)
                    )
                    description.value =
                        TextFieldValue(it.description, TextRange(it.description.length))
                    difficulty.value = it.difficultyLevel
                    targetAudience.value = it.targetAudience
                    cancellationPolicy.value = it.cancellationPolicy

                    images = it.images

                    eventType = it.eventType
                    url = TextFieldValue(it.onlineUrl, TextRange(it.onlineUrl.length))

                    if (it.latitude != 0.0) {
                        latitude = TextFieldValue(
                            it.latitude.toString(),
                            TextRange(it.latitude.toString().length)
                        )
                    }

                    if (it.longitude != 0.0) {
                        longitude = TextFieldValue(
                            it.longitude.toString(),
                            TextRange(it.longitude.toString().length)
                        )
                    }
                }
        }
    }

    fun addImage(uri: Uri) {
        images = images.toMutableList().apply {
            add(
                YogaImage(
                    id = UUID.randomUUID().toString(),
                    courseId = "",
                    bitmap = imageUtils.createBitmap(uri),
                    base64 = imageUtils.imageUriToBase64(uri).orEmpty()
                )
            )
        }
    }

    fun removeImage(id: String) {
        images = images.toMutableList().apply {
            removeIf { it.id == id }
        }
    }

    fun useCurrentLocation() {
        viewModelScope.launch {
            locationHelper.getCurrentLocation()?.let { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                latitude = TextFieldValue(lat, TextRange(lat.length))
                longitude = TextFieldValue(long, TextRange(long.length))
            }
        }
    }

    // Updates the event type and clears other inputs accordingly.
    fun updateEventType(eventType: YogaEventType) {
        this.eventType = eventType
        // clear other inputs when event type has changed
        when (eventType) {
            YogaEventType.ONLINE -> {
                latitude = TextFieldValue()
                longitude = TextFieldValue()
            }

            YogaEventType.IN_PERSON -> {
                url = TextFieldValue()
            }

            YogaEventType.UNSPECIFIED -> {
                latitude = TextFieldValue()
                longitude = TextFieldValue()
                url = TextFieldValue()
            }
        }
    }

    // Validates inputs and shows confirmation dialog if valid.
    fun onSave() {
        val error = validateInputs()
        if (error != null) {
            inputError.value = error
            return
        }
        showConfirmSave = true
    }

    // Creates or updates the yoga course.
    fun create() {
        viewModelScope.launch {
            val newCourseId = courseId ?: UUID.randomUUID().toString()
            val course = YogaCourse(
                id = newCourseId,

                dayOfWeek = selectedDayOfWeek.value,
                time = time.value.text.trim(),
                duration = duration.value.text.trim(),

                typeOfClass = classType.value,
                difficultyLevel = difficulty.value,
                targetAudience = targetAudience.value,
                description = description.value.text,

                capacity = capacity.value.text.toInt(),
                pricePerClass = price.value.text.toDouble(),
                cancellationPolicy = cancellationPolicy.value,

                classes = emptyList(),

                images = images.map {
                    YogaImage(
                        id = it.id,
                        courseId = newCourseId,
                        bitmap = it.bitmap,
                        base64 = it.base64
                    )
                },

                eventType = eventType,
                onlineUrl = url.text.trim(),
                latitude = latitude.text.trim().toDoubleOrNull() ?: 0.0,
                longitude = longitude.text.trim().toDoubleOrNull() ?: 0.0,
            )
            repo.createCourse(course)
                .onSuccess { navigateToHome.value = true }
        }
    }

    fun resetInputError() {
        inputError.value = null
    }

    // Validates course inputs and returns an error if any are invalid.
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
        private val courseId: String? = null,
        private val repo: YogaRepository,
        private val imageUtils: ImageUtils,
        private val locationHelper: LocationHelper,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return CreateCourseScreenViewModel(
                courseId = courseId,
                repo = repo,
                imageUtils = imageUtils,
                locationHelper = locationHelper
            ) as T
        }
    }
}