package com.myolwinoo.universalyoga.admin.data.model

/**
 * Represents the type of event for a yoga class or course.
 */
enum class YogaEventType(val displayName: String) {
    ONLINE("Online"),
    IN_PERSON("In-Person"),
    UNSPECIFIED("Unspecified")
}