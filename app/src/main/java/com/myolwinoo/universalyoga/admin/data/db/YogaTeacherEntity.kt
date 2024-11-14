package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a yoga teacher entity in the database.
 */
@Entity(tableName = "yoga_teachers")
data class YogaTeacherEntity(
    @PrimaryKey
    val teacherId: String,
    val name: String,
)
