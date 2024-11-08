package com.myolwinoo.universalyoga.admin.data.model

data class YogaCourse(
    val id: String,

    val dayOfWeek: DayOfWeek,
    val time: String,
    val duration: String,

    val capacity: Int,
    val pricePerClass: Double,

    val typeOfClass: YogaClassType,
    val description: String,
    val difficultyLevel: DifficultyLevel,
    val targetAudience: TargetAudience,

    val cancellationPolicy: CancellationPolicy,

    val classes: List<YogaClass>
)
