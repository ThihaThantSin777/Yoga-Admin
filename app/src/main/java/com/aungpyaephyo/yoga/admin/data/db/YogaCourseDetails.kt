package com.aungpyaephyo.yoga.admin.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation


data class YogaCourseDetails(
    @Embedded
    val course: YogaCourseEntity,
    @Relation(
        entity = YogaClassEntity::class,
        parentColumn = "id",
        entityColumn = "courseId"
    )
    val yogaClasses: List<YogaClassDetails>,
//    @Relation(
//        entity = YogaImageEntity::class,
//        parentColumn = "id",
//        entityColumn = "courseId"
//    )
//    val images: List<YogaImageEntity>
)

data class YogaClassDetails(
    @Embedded
    val yogaClass: YogaClassEntity,
    @Relation(
        parentColumn = "classId",
        entityColumn = "teacherId",
        associateBy = Junction(YogaClassTeacherCrossRef::class)
    )
    val teachers: List<YogaTeacherEntity>
)

@Entity(
    tableName = "yoga_class_teachers",
    primaryKeys = ["classId", "teacherId"],
    indices = [Index("teacherId")]
)
data class YogaClassTeacherCrossRef(
    val classId: String,
    val teacherId: String
)