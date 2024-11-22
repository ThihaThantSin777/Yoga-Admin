package com.aungpyaephyo.yoga.admin.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

data class YogaClass(
    val id: String,
    val date: String,
    val courseId: String,
    val comment: String,
    val teachers: List<String>
) {
    val teacherNames = teachers.joinToString(", ")
    val dayOfWeek = date.let {
        LocalDate.parse(it, LocalDate.Format {
            dayOfMonth()
            char('/')
            monthNumber()
            char('/')
            year()
        }).dayOfWeek
    }
}
