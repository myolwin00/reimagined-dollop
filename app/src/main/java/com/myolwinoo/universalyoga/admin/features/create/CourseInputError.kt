package com.myolwinoo.universalyoga.admin.features.create

sealed class CourseInputError {
    data object StartTime: CourseInputError()
    data object Duration: CourseInputError()
    data object Capacity: CourseInputError()
    data object Price: CourseInputError()
}