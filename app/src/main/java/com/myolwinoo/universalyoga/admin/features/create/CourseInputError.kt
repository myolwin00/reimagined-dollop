package com.myolwinoo.universalyoga.admin.features.create

/**
 * Represents the possible input errors that can occur during course creation.
 */
sealed class CourseInputError {
    data object StartTime: CourseInputError()
    data object Duration: CourseInputError()
    data object Capacity: CourseInputError()
    data object Price: CourseInputError()
}