package com.myolwinoo.universalyoga.admin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface YogaDao {

    @Query("SELECT * FROM yoga_courses")
    fun getAllCourses(): Flow<List<YogaCourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: YogaCourseEntity)
}