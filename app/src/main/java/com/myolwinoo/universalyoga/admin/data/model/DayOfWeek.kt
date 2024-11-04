package com.myolwinoo.universalyoga.admin.data.model;

enum class DayOfWeek(val displayName: String) {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }

        fun fromDisplayName(name: String): DayOfWeek? = entries.find { it.displayName.equals(name, ignoreCase = true) }
    }
}