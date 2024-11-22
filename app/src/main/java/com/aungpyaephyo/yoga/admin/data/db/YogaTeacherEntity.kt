package com.aungpyaephyo.yoga.admin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yoga_teachers")
data class YogaTeacherEntity(
    @PrimaryKey
    val teacherId: String,
    val name: String,
)
