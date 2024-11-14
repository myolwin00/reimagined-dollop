package com.myolwinoo.universalyoga.admin.data.model

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude

/**
 * Data class representing an image associated with a yoga course.
 */
data class YogaImage(
    val id: String,
    val courseId: String,
    @Exclude
    val bitmap: Bitmap?,
    /**
     * Base64 encoded string representation of the image.
     */
    val base64: String
)