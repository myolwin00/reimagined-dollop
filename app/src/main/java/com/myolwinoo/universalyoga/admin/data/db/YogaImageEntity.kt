package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a yoga image entity in the database.
 */
@Entity(tableName = "yoga_images")
data class YogaImageEntity(
    @PrimaryKey
    val id: String,
    val base64: String,
    val courseId: String
)