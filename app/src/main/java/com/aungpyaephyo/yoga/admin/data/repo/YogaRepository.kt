package com.aungpyaephyo.yoga.admin.data.repo
import com.aungpyaephyo.yoga.admin.data.db.YogaClassEntity
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.data.db.YogaDao

import com.aungpyaephyo.yoga.admin.data.db.YogaTeacherEntity
import com.aungpyaephyo.yoga.admin.data.db.YogaClassDetails
import com.aungpyaephyo.yoga.admin.data.db.YogaClassTeacherCrossRef
import com.aungpyaephyo.yoga.admin.data.db.YogaCourseDetails
import com.aungpyaephyo.yoga.admin.data.db.YogaCourseEntity
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class YogaRepository(
    private val yogaDao: YogaDao,

) {

    fun getAllCourses(): Flow<List<YogaCourse>> {
        return yogaDao.getAllCourses()
            .map { it.map(::mapYogaCourse) }
    }

    fun getCourseDetails(courseId: String): Flow<YogaCourse?> {
        return yogaDao.getCourseDetails(courseId)
            .map { it?.let(::mapYogaCourse) }
    }

    suspend fun getCourse(courseId: String): YogaCourse? {
        return yogaDao.getCourse(courseId)?.let(::mapYogaCourse)
    }

    fun getTeacherNameSuggestions(teacherName: String): Flow<List<Pair<String, String>>> {
        return yogaDao.searchTeacher(teacherName)
            .map { it.map { teacher -> teacher.teacherId to teacher.name } }
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
//            cancellationPolicy = course.cancellationPolicy,
//            targetAudience = course.targetAudience,
            eventType = course.eventType,
            latitude = course.latitude,
            longitude = course.longitude,
            onlineUrl = course.onlineUrl
        )
//        val imageEntities = course.images.map {
//            YogaImageEntity(
//                id = it.id,
//                courseId = it.courseId,
//                base64 = it.base64
//            )
//        }
        return try {
           // yogaDao.deleteYogaCourseImages(course.id)
         //   yogaDao.insertYogaImage(*imageEntities.toTypedArray())
            yogaDao.insertCourse(entity)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun deleteCourse(courseId: String) {
        yogaDao.deleteCourseClass(courseId)
        yogaDao.deleteCourse(courseId)
    }

    fun getAllYogaClasses(): Flow<List<YogaClass>> {
        return yogaDao.getAllClasses()
            .map { it.map(::mapYogaClass) }
    }

    suspend fun deleteClass(classId: String) {
        yogaDao.deleteClass(classId)
    }

    suspend fun getYogaClass(classId: String): YogaClass? {
        return yogaDao.getClass(classId)?.let(::mapYogaClass)
    }

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

    fun mapYogaClass(classDetails: YogaClassDetails): YogaClass {
        return YogaClass(
            id = classDetails.yogaClass.classId,
            date = classDetails.yogaClass.date,
            comment = classDetails.yogaClass.comment,
            teachers = classDetails.teachers.map { it.name },
            courseId = classDetails.yogaClass.courseId
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
//            cancellationPolicy = entity.course.cancellationPolicy,
//            targetAudience = entity.course.targetAudience,
            classes = entity.yogaClasses.map(::mapYogaClass),
//            images = entity.images.map {
//                YogaImage(
//                    id = it.id,
//                    courseId = it.courseId,
//                    base64 = it.base64,
//                    bitmap = imageUtils.base64ToBitmap(it.base64)
//                )
//            },
            eventType = entity.course.eventType,
            latitude = entity.course.latitude,
            longitude = entity.course.longitude,
            onlineUrl = entity.course.onlineUrl
        )
    }

    fun mapYogaCourse(entity: YogaCourseEntity): YogaCourse {
        return YogaCourse(
            id = entity.id,
            dayOfWeek = entity.dayOfWeek,
            time = entity.time,
            capacity = entity.capacity,
            duration = entity.duration,
            pricePerClass = entity.pricePerClass,
            typeOfClass = entity.typeOfClass,
            description = entity.description,
            difficultyLevel = entity.difficultyLevel,
//            cancellationPolicy = entity.cancellationPolicy,
//            targetAudience = entity.targetAudience,
            classes = emptyList(),
//            images = emptyList(),
            eventType = entity.eventType,
            latitude = entity.latitude,
            longitude = entity.longitude,
            onlineUrl = entity.onlineUrl
        )
    }
}