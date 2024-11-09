package com.myolwinoo.universalyoga.admin.features.yogaclass.create

sealed class ClassInputError {
    data object TeacherName: ClassInputError()
    data object Date: ClassInputError()
}