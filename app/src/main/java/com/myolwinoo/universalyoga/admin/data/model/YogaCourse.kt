package com.myolwinoo.universalyoga.admin.data.model

data class YogaCourse(
    val id: String,
    val dayOfWeek: DayOfWeek,
    val time: String,
    val capacity: Int,
    val duration: Int,
    val pricePerClass: Double,
    val typeOfClass: YogaClassType,
    val description: String,
    val difficultyLevel: DifficultyLevel,
    val cancellationPolicy: CancellationPolicy,
    val targetAudience: TargetAudience,
)
