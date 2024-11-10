package com.myolwinoo.universalyoga.admin.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

data class YogaClass(
    val id: String,
    val date: String,
    val courseId: String,
    val teacherId: String,
    val teacherName: String,
    val comment: String
) {
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
