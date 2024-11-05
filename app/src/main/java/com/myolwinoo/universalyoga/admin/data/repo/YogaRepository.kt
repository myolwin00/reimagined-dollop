package com.myolwinoo.universalyoga.admin.data.repo

import com.myolwinoo.universalyoga.admin.data.db.YogaCourseEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaDao
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class YogaRepository(
    private val yogaDao: YogaDao
) {

    fun getAllCourses(): Flow<List<YogaCourse>> {
        return yogaDao.getAllCourses()
            .map {
                it.map { entity ->
                    YogaCourse(
                        id = entity.id,
                        dayOfWeek = entity.dayOfWeek,
                        time = entity.time,
                        capacity = entity.capacity,
                        duration = entity.duration,
                        pricePerClass = entity.pricePerClass,
                        typeOfClass = entity.typeOfClass,
                        description = entity.description,
                        difficultyLevel = entity.difficultyLevel,
                        cancellationPolicy = entity.cancellationPolicy,
                        targetAudience = entity.targetAudience,
                    )
                }
            }
    }

    suspend fun createCourse(
        course: YogaCourse
    ): Result<Unit> {
        val entity = YogaCourseEntity(
            id = course.id,
            dayOfWeek = course.dayOfWeek,
            time = course.time,
            capacity = course.capacity,
            duration = course.duration,
            pricePerClass = course.pricePerClass,
            typeOfClass = course.typeOfClass,
            description = course.description,
            difficultyLevel = course.difficultyLevel,
            cancellationPolicy = course.cancellationPolicy,
            targetAudience = course.targetAudience,
        )
        return try {
            yogaDao.insertAll(entity)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}