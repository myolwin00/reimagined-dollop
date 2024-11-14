package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.DifficultyLevel
import com.myolwinoo.universalyoga.admin.data.model.TargetAudience
import com.myolwinoo.universalyoga.admin.data.model.YogaClassType
import com.myolwinoo.universalyoga.admin.data.model.YogaEventType
import kotlinx.datetime.DayOfWeek

/**
 * Data class representing a yoga course entity in the database.
 */
@Entity(tableName = "yoga_courses")
data class YogaCourseEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: DayOfWeek,
    val time: String,
    val capacity: Int,
    val duration: String,
    @ColumnInfo(name = "price_per_class")
    val pricePerClass: Double,
    @ColumnInfo(name = "type_of_class")
    val typeOfClass: YogaClassType,
    val description: String,
    @ColumnInfo(name = "difficulty_level")
    val difficultyLevel: DifficultyLevel,
    @ColumnInfo(name = "cancellation_policy")
    val cancellationPolicy: CancellationPolicy,
    @ColumnInfo(name = "target_audience")
    val targetAudience: TargetAudience,
    @ColumnInfo(name = "event_type")
    val eventType: YogaEventType,
    val latitude: Double,
    val longitude: Double,
    val onlineUrl: String
)
