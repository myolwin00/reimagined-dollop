package com.myolwinoo.universalyoga.admin.utils

import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.DayOfWeek
import com.myolwinoo.universalyoga.admin.data.model.DifficultyLevel
import com.myolwinoo.universalyoga.admin.data.model.TargetAudience
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaClassType
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Random
import java.util.UUID
import kotlin.text.format

/**
 * A data provider that generates dummy yoga course data for development and testing purposes.
 */
object DummyDataProvider {

    val dummyYogaCourses = generateSampleYogaCourses()

    /**
     * Generates a list of sample yoga courses.
     *
     * @return A list of [YogaCourse] objects representing sample yoga courses.
     */
    private fun generateSampleYogaCourses(): List<YogaCourse> {
        val courses = mutableListOf<YogaCourse>()
        val random = Random()

        for (i in 1..10) {
            val id = UUID.randomUUID().toString()
            val dayOfWeek = DayOfWeek.entries[random.nextInt(DayOfWeek.entries.size)]
            val time = "${(9 + random.nextInt(6)).toString().padStart(2, '0')}:00" // Random time between 09:00 and 15:00
            val capacity = 10 + random.nextInt(15)
            val duration = 60 + random.nextInt(30) // Duration in minutes (60 to 90)
            val pricePerClass = 15.0 + random.nextDouble() * 10.0 // Price between $15.0 and $25.0
            val typeOfClass = YogaClassType.entries[random.nextInt(YogaClassType.entries.size)]
            val description = "A \"${typeOfClass.displayName}\" yoga class for ${
                listOf("beginners", "all levels", "intermediate students")[random.nextInt(3)]
            }."
            val difficultyLevel = DifficultyLevel.entries[random.nextInt(DifficultyLevel.entries.size)]
            val cancellationPolicy = CancellationPolicy.entries[random.nextInt(CancellationPolicy.entries.size)]
            val targetAudience = TargetAudience.entries[random.nextInt(TargetAudience.entries.size)]

            val classes = generateSampleYogaClasses()

            courses.add(
                YogaCourse(
                    id = id,
                    dayOfWeek = dayOfWeek,
                    time = time,
                    capacity = capacity,
                    duration = duration.toString(),
                    pricePerClass = pricePerClass,
                    typeOfClass = typeOfClass,
                    description = description,
                    difficultyLevel = difficultyLevel,
                    cancellationPolicy = cancellationPolicy,
                    targetAudience = targetAudience,
                    classes = classes
                )
            )
        }

        return courses
    }
}

/**
 * Generates a list of sample yoga classes.
 *
 * @return A list of [YogaClass] objects representing sample yoga classes.
 */
fun generateSampleYogaClasses(): List<YogaClass> {
    val classes = mutableListOf<YogaClass>()
    val random = Random()
    val teachers = listOf("Amy", "John", "Sarah", "David")

    for (i in 1..4) {
        val id = UUID.randomUUID().toString()
        val date = "2023-12-19"
        val teacher = teachers[random.nextInt(teachers.size)]
        val comment = listOf(
            "Great class!",
            "Relaxing and rejuvenating.",
            "Challenging but rewarding.",
            "Loved the flow."
        )[random.nextInt(4)]

        classes.add(
            YogaClass(
                id = id,
                date = date,
                teacherName = teacher,
                teacherId = "",
                courseId = "",
                comment = comment
            )
        )
    }

    return classes
}