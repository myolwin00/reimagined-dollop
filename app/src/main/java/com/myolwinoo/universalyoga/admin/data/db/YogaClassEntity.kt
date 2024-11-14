package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a yoga class entity in the database.
 */
@Entity(tableName = "yoga_classes")
data class YogaClassEntity(
    @PrimaryKey
    val classId: String,
    val date: String,
    val comment: String,
    val courseId: String
)
