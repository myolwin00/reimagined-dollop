package com.myolwinoo.universalyoga.admin.data.model;

/**
 * Represents the different target audiences for yoga classes.
 */
enum class TargetAudience(val displayName: String) {
    ADULTS("Adults"),
    SENIORS("Seniors"),
    KIDS("Kids"),
    TEENS("Teens"),
    FAMILIES("Families"),
    BEGINNERS("Beginners"),
    ADVANCED_PRACTITIONERS("Advanced Practitioners"),
    PRENATAL("Prenatal"),
    CORPORATE_GROUPS("Corporate Groups"),
    ALL("All");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }
    }
}