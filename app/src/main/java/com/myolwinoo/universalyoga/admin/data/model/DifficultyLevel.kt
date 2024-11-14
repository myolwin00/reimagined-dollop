package com.myolwinoo.universalyoga.admin.data.model;

/**
 * Represents the different difficulty levels for yoga classes.
 */
enum class DifficultyLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }
    }
}