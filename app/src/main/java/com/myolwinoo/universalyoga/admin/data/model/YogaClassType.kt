package com.myolwinoo.universalyoga.admin.data.model;

/**
 * Represents the different types of yoga classes.
 */
enum class YogaClassType(val displayName: String) {
    FLOW_YOGA("Flow Yoga"),
    AERIAL_YOGA("Aerial Yoga"),
    FAMILY_YOGA("Family Yoga"),
    POWER_YOGA("Power Yoga"),
    HATHA_YOGA("Hatha Yoga"),
    ASHTANGA_YOGA("Ashtanga Yoga"),
    YIN_YOGA("Yin Yoga");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }

        fun fromDisplayName(name: String): YogaClassType? = entries.find { it.displayName.equals(name, ignoreCase = true) }
    }
}