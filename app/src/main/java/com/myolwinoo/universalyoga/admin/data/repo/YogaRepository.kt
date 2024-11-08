package com.myolwinoo.universalyoga.admin.data.repo

import com.myolwinoo.universalyoga.admin.data.db.YogaClassDetails
import com.myolwinoo.universalyoga.admin.data.db.YogaClassEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaClassTeacherCrossRef
import com.myolwinoo.universalyoga.admin.data.db.YogaCourseDetails
import com.myolwinoo.universalyoga.admin.data.db.YogaCourseEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaDao
import com.myolwinoo.universalyoga.admin.data.db.YogaTeacherEntity
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class YogaRepository(
    private val yogaDao: YogaDao
) {

    fun getAllCourses(): Flow<List<YogaCourse>> {
        return yogaDao.getAllCourses()
            .map { it.map(::mapYogaCourse) }
    }

    fun getCourse(courseId: String): Flow<YogaCourse?> {
        return yogaDao.getCourse(courseId)
            .map { it?.let(::mapYogaCourse) }
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
            yogaDao.insertCourse(entity)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    fun getYogaClasses(courseId: String): Flow<List<YogaClass>> {
        return yogaDao.getClasses(courseId)
            .map { it.map(::mapYogaClass) }
    }

    suspend fun createYogaClass(yogaClass: YogaClass): Result<Unit> {

        val classEntity = YogaClassEntity(
            classId = yogaClass.id,
            date = yogaClass.date,
            comment = yogaClass.comment,
            courseId = yogaClass.courseId
        )
        return try {
            val teacherId = saveTeacherIfNotExists(
                teacherId = yogaClass.teacherId,
                teacherName = yogaClass.teacherName
            )
            yogaDao.insertClass(classEntity)
            yogaDao.insertYogaClassTeacher(
                YogaClassTeacherCrossRef(
                    classId = yogaClass.id,
                    teacherId = teacherId
                )
            )
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun saveTeacherIfNotExists(
        teacherId: String,
        teacherName: String
    ): String {
        val teacherEntity = YogaTeacherEntity(
            teacherId = teacherId,
            name = teacherName
        )
        return yogaDao.findTeacherByName(teacherName).let {
            if (it == null) {
                yogaDao.insertTeacher(teacherEntity)
                teacherId
            } else {
                it.teacherId
            }
        }
    }

    fun mapYogaClass(classDetails: YogaClassDetails): YogaClass {
        return YogaClass(
            id = classDetails.yogaClass.classId,
            date = classDetails.yogaClass.date,
            comment = classDetails.yogaClass.comment,
            teacherId = classDetails.teachers.firstOrNull()?.teacherId.orEmpty(),
            teacherName = classDetails.teachers.firstOrNull()?.name.orEmpty(),
            courseId = classDetails.yogaClass.courseId
        )
    }

    fun mapYogaClass(entity: YogaClassEntity): YogaClass {
        return YogaClass(
            id = entity.classId,
            date = entity.date,
            comment = entity.comment,
            teacherId = "",
            teacherName = "",
            courseId = entity.courseId
        )
    }

    fun mapYogaCourse(entity: YogaCourseDetails): YogaCourse {
        return YogaCourse(
            id = entity.course.id,
            dayOfWeek = entity.course.dayOfWeek,
            time = entity.course.time,
            capacity = entity.course.capacity,
            duration = entity.course.duration,
            pricePerClass = entity.course.pricePerClass,
            typeOfClass = entity.course.typeOfClass,
            description = entity.course.description,
            difficultyLevel = entity.course.difficultyLevel,
            cancellationPolicy = entity.course.cancellationPolicy,
            targetAudience = entity.course.targetAudience,
            classes = entity.yogaClasses.map(::mapYogaClass)
        )
    }
}