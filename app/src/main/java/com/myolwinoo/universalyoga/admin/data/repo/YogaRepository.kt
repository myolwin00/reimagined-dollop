package com.myolwinoo.universalyoga.admin.data.repo

import com.myolwinoo.universalyoga.admin.data.db.YogaClassDetails
import com.myolwinoo.universalyoga.admin.data.db.YogaClassEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaClassTeacherCrossRef
import com.myolwinoo.universalyoga.admin.data.db.YogaCourseDetails
import com.myolwinoo.universalyoga.admin.data.db.YogaCourseEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaDao
import com.myolwinoo.universalyoga.admin.data.db.YogaImageEntity
import com.myolwinoo.universalyoga.admin.data.db.YogaTeacherEntity
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.model.YogaImage
import com.myolwinoo.universalyoga.admin.utils.ImageUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Repository class for managing yoga data.
 *
 * This class provides methods for accessing and manipulating yoga courses, classes, teachers, and images.
 * It interacts with the database through the [YogaDao] and handles image conversions using [ImageUtils].
 */
class YogaRepository(
    private val yogaDao: YogaDao,
    private val imageUtils: ImageUtils
) {

    /**
     * Retrieves all yoga courses as a flow.
     */
    fun getAllCourses(): Flow<List<YogaCourse>> {
        return yogaDao.getAllCourses()
            .map { it.map(::mapYogaCourse) }
    }

    /**
     * Retrieves the details of a specific yoga course by its ID as a flow.
     */
    fun getCourseDetails(courseId: String): Flow<YogaCourse?> {
        return yogaDao.getCourseDetails(courseId)
            .map { it?.let(::mapYogaCourse) }
    }

    /**
     * Retrieves a specific yoga course by its ID.
     */
    suspend fun getCourse(courseId: String): YogaCourse? {
        return yogaDao.getCourse(courseId)?.let(::mapYogaCourse)
    }

    /**
     * Retrieves teacher name suggestions based on a given query as a flow.
     */
    fun getTeacherNameSuggestions(teacherName: String): Flow<List<Pair<String, String>>> {
        return yogaDao.searchTeacher(teacherName)
            .map { it.map { teacher -> teacher.teacherId to teacher.name } }
    }

    /**
     * Creates a new yoga course or update if exists.
     */
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
            eventType = course.eventType,
            latitude = course.latitude,
            longitude = course.longitude,
            onlineUrl = course.onlineUrl
        )
        val imageEntities = course.images.map {
            YogaImageEntity(
                id = it.id,
                courseId = it.courseId,
                base64 = it.base64
            )
        }
        return try {
            yogaDao.deleteYogaCourseImages(course.id)
            yogaDao.insertYogaImage(*imageEntities.toTypedArray())
            yogaDao.insertCourse(entity)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    /**
     * Deletes a yoga course by its ID.
     */
    suspend fun deleteCourse(courseId: String) {
        yogaDao.deleteCourseClass(courseId)
        yogaDao.deleteCourse(courseId)
    }

    /**
     * Retrieves all yoga classes as a flow.
     */
    fun getAllYogaClasses(): Flow<List<YogaClass>> {
        return yogaDao.getAllClasses()
            .map { it.map(::mapYogaClass) }
    }

    /**
     * Deletes a yoga class by its ID.
     */
    suspend fun deleteClass(classId: String) {
        yogaDao.deleteClass(classId)
    }

    /**
     * Retrieves a specific yoga class by its ID.
     */
    suspend fun getYogaClass(classId: String): YogaClass? {
        return yogaDao.getClass(classId)?.let(::mapYogaClass)
    }

    /**
     * Creates a new yoga class if exists.
     */
    suspend fun createYogaClass(yogaClass: YogaClass): Result<Unit> {
        return try {
            // insert teachers to teacher table if they don't exist
            val teacherIds = yogaClass.teachers.map { teacherName ->
                saveTeacherIfNotExists(teacherName = teacherName)
            }
            // insert class information
            yogaDao.insertClass(
                YogaClassEntity(
                    classId = yogaClass.id,
                    date = yogaClass.date,
                    comment = yogaClass.comment,
                    courseId = yogaClass.courseId
                )
            )
            // clear old class teacher relations
            yogaDao.deleteYogaClassTeacher(yogaClass.id)
            // insert new class teacher relations
            teacherIds.forEach { teacherId ->
                yogaDao.insertYogaClassTeacher(
                    YogaClassTeacherCrossRef(
                        classId = yogaClass.id,
                        teacherId = teacherId
                    )
                )
            }
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    /**
     * Saves a teacher to the database if they don't already exist.
     */
    private suspend fun saveTeacherIfNotExists(
        teacherName: String
    ): String {
        return yogaDao.findTeacherByName(teacherName).let {
            if (it == null) {
                val teacherId = UUID.randomUUID().toString()
                val teacherEntity = YogaTeacherEntity(
                    teacherId = teacherId,
                    name = teacherName
                )
                yogaDao.insertTeacher(teacherEntity)
                teacherId
            } else {
                it.teacherId
            }
        }
    }

    /**
     * Maps a [YogaClassDetails] db object to a [YogaClass] domain object.
     */
    fun mapYogaClass(classDetails: YogaClassDetails): YogaClass {
        return YogaClass(
            id = classDetails.yogaClass.classId,
            date = classDetails.yogaClass.date,
            comment = classDetails.yogaClass.comment,
            teachers = classDetails.teachers.map { it.name },
            courseId = classDetails.yogaClass.courseId
        )
    }

    /**
     * Maps a [YogaCourseDetails] db object to a [YogaCourse] domain object.
     */
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
            classes = entity.yogaClasses.map(::mapYogaClass),
            images = entity.images.map {
                YogaImage(
                    id = it.id,
                    courseId = it.courseId,
                    base64 = it.base64,
                    bitmap = imageUtils.base64ToBitmap(it.base64)
                )
            },
            eventType = entity.course.eventType,
            latitude = entity.course.latitude,
            longitude = entity.course.longitude,
            onlineUrl = entity.course.onlineUrl
        )
    }
}