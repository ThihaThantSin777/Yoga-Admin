package com.aungpyaephyo.yoga.admin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

import kotlinx.coroutines.flow.Flow

@Dao
interface YogaDao {

    // yoga courses
    @Transaction
    @Query("select * from yoga_courses")
    fun getAllCourses(): Flow<List<YogaCourseDetails>>

    @Transaction
    @Query("select * from yoga_courses where id = :id")
    fun getCourseDetails(id: String): Flow<YogaCourseDetails?>

    @Transaction
    @Query("select * from yoga_courses where id = :id")
    suspend fun getCourse(id: String): YogaCourseDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(vararg entity: YogaCourseEntity)

    @Query("delete from yoga_courses where id = :id")
    suspend fun deleteCourse(id: String): Int

    // yoga classes
    @Transaction
    @Query("select * from yoga_classes")
    fun getAllClasses(): Flow<List<YogaClassDetails>>

    @Transaction
    @Query("select * from yoga_classes where courseid = :courseId")
    fun getClasses(courseId: String): Flow<List<YogaClassDetails>>

    @Transaction
    @Query("select * from yoga_classes where classId = :classId")
    suspend fun getClass(classId: String): YogaClassDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(vararg entity: YogaClassEntity)

    @Query("delete from yoga_classes where classId = :id")
    suspend fun deleteClass(id: String): Int

    @Query("delete from yoga_classes where courseId = :courseId")
    suspend fun deleteCourseClass(courseId: String): Int

    // yoga teachers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(vararg entity: YogaTeacherEntity)

    @Query("select * from yoga_teachers where teacherId = :id")
    suspend fun getTeacher(id: String): YogaTeacherEntity

    @Query("select * from yoga_teachers where name = :name")
    suspend fun findTeacherByName(name: String): YogaTeacherEntity?

    @Query("select * from yoga_teachers where name like '%' || :query || '%'")
    fun searchTeacher(query: String): Flow<List<YogaTeacherEntity>>

    // yoga classes and teachers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYogaClassTeacher(vararg entity: YogaClassTeacherCrossRef)

    @Query("delete from yoga_class_teachers where classId = :classId")
    suspend fun deleteYogaClassTeacher(classId: String): Int


}