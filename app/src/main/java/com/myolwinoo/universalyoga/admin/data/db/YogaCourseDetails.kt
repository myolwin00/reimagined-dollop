package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class representing a yoga course with its related details, including classes and images.
 */
data class YogaCourseDetails(
    @Embedded
    val course: YogaCourseEntity,

    /**
     * List of yoga classes associated with this course.
     */
    @Relation(
        entity = YogaClassEntity::class,
        parentColumn = "id",
        entityColumn = "courseId"
    )
    val yogaClasses: List<YogaClassDetails>,

    /**
     * List of images associated with this course.
     */
    @Relation(
        entity = YogaImageEntity::class,
        parentColumn = "id",
        entityColumn = "courseId"
    )
    val images: List<YogaImageEntity>
)

/**
 * Data class representing details of a yoga class, including its associated teachers.
 */
data class YogaClassDetails(
    @Embedded
    val yogaClass: YogaClassEntity,

    /**
     * List of teachers who teach this class.
     */
    @Relation(
        parentColumn = "classId",
        entityColumn = "teacherId",
        associateBy = Junction(YogaClassTeacherCrossRef::class)
    )
    val teachers: List<YogaTeacherEntity>
)

/**
 * Cross-reference entity linking yoga classes and teachers.
 */
@Entity(
    tableName = "yoga_class_teachers",
    primaryKeys = ["classId", "teacherId"],
    indices = [Index("teacherId")]
)
data class YogaClassTeacherCrossRef(
    val classId: String,
    val teacherId: String
)