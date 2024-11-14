package com.myolwinoo.universalyoga.admin.features.yogaclass.create

/**
 * Represents the possible input errors that can occur during yoga class creation.
 */
sealed class ClassInputError {
    data object TeacherName: ClassInputError()
    data object Date: ClassInputError()
}