package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for interacting with the Yoga database.
 */
@Dao
interface YogaDao {

    // yoga courses
    /**
     * Retrieves all yoga courses with their details as a flow.
     */
    @Transaction
    @Query("select * from yoga_courses")
    fun getAllCourses(): Flow<List<YogaCourseDetails>>

    /**
     * Retrieves the details of a specific yoga course by its ID as a flow.
     */
    @Transaction
    @Query("select * from yoga_courses where id = :id")
    fun getCourseDetails(id: String): Flow<YogaCourseDetails?>

    /**
     * Retrieves a specific yoga course by its ID.
     */
    @Transaction
    @Query("select * from yoga_courses where id = :id")
    suspend fun getCourse(id: String): YogaCourseDetails?

    /**
     * Inserts one or more yoga courses into the database.
     * If a course with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(vararg entity: YogaCourseEntity)

    /**
     * Deletes a yoga course by its ID.
     */
    @Query("delete from yoga_courses where id = :id")
    suspend fun deleteCourse(id: String): Int

    // yoga classes
    /**
     * Retrieves all yoga classes with their details as a flow.
     */
    @Transaction
    @Query("select * from yoga_classes")
    fun getAllClasses(): Flow<List<YogaClassDetails>>

    /**
     * Retrieves all yoga classes for a specific course ID as a flow.
     */
    @Transaction
    @Query("select * from yoga_classes where courseid = :courseId")
    fun getClasses(courseId: String): Flow<List<YogaClassDetails>>

    /**
     * Retrieves a specific yoga class by its ID.
     */
    @Transaction
    @Query("select * from yoga_classes where classId = :classId")
    suspend fun getClass(classId: String): YogaClassDetails?

    /**
     * Inserts one or more yoga classes into the database.
     * If a class with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(vararg entity: YogaClassEntity)

    /**
     * Deletes a yoga class by its ID.
     */
    @Query("delete from yoga_classes where classId = :id")
    suspend fun deleteClass(id: String): Int

    /**
     * Deletes all yoga classes associated with a specific course ID.
     */
    @Query("delete from yoga_classes where courseId = :courseId")
    suspend fun deleteCourseClass(courseId: String): Int

    // yoga teachers
    /**
     * Inserts one or more yoga teachers into the database.
     * If a teacher with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(vararg entity: YogaTeacherEntity)

    /**
     * Retrieves a specific yoga teacher by their ID.
     */
    @Query("select * from yoga_teachers where teacherId = :id")
    suspend fun getTeacher(id: String): YogaTeacherEntity

    /**
     * Finds a yoga teacher by their name.
     */
    @Query("select * from yoga_teachers where name = :name")
    suspend fun findTeacherByName(name: String): YogaTeacherEntity?

    /**
     * Searches for yoga teachers whose names contain the given query.
     */
    @Query("select * from yoga_teachers where name like '%' || :query || '%'")
    fun searchTeacher(query: String): Flow<List<YogaTeacherEntity>>

    // yoga classes and teachers
    /**
     * Inserts one or more yoga class-teacher cross-references into the database.
     * If a cross-reference with the same class ID and teacher ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYogaClassTeacher(vararg entity: YogaClassTeacherCrossRef)

    /**
     * Deletes all yoga class-teacher cross-references for a specific class ID.
     */
    @Query("delete from yoga_class_teachers where classId = :classId")
    suspend fun deleteYogaClassTeacher(classId: String): Int

    // yoga images
    /**
     * Inserts one or more yoga images into the database.
     * If an image with the same ID already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYogaImage(vararg entity: YogaImageEntity)

    /**
     * Deletes all yoga images associated with a specific course ID.
     */
    @Query("delete from yoga_images where courseId = :courseId")
    suspend fun deleteYogaCourseImages(courseId: String): Int
}