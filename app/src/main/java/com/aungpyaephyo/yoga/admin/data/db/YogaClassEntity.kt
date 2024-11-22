package com.aungpyaephyo.yoga.admin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yoga_classes")
data class YogaClassEntity(
    @PrimaryKey
    val classId: String,
    val date: String,
    val comment: String,
    val courseId: String
)
