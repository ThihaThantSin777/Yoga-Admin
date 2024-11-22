package com.aungpyaephyo.yoga.admin.data.model

enum class DifficultyLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }
    }
}