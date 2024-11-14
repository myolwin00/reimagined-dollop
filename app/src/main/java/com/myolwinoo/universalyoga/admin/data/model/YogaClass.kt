package com.myolwinoo.universalyoga.admin.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

/**
 * Data class representing a single yoga class.
 */
data class YogaClass(
    val id: String,
    val date: String,
    val courseId: String,
    val comment: String,
    val teachers: List<String>
) {
    /**
     * A string containing all teacher names separated by commas.
     */
    val teacherNames = teachers.joinToString(", ")

    /**
     * Day of the week the class is held, derived from the date.
     */
    val dayOfWeek = date.let {
        LocalDate.parse(it, LocalDate.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        }).dayOfWeek
    }
}
