package com.myolwinoo.universalyoga.admin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class for working with images.
 *
 * This class provides methods for creating image files, converting between URIs, Bitmaps, and Base64 strings,
 * and managing the URI of the current photo.
 */
class ImageUtils(
    private val context: Context
) {
    var currentPhotoUri: Uri? = null
        private set

    fun resetCurrentPhotoUri() {
        currentPhotoUri = null
    }

    /**
     * Creates a URI for a new image file.
     *
     * This method creates a temporary image file in the application's files directory and returns a URI
     * that can be used to access the file.
     *
     * @return The URI of the new image file, or null if an error occurred.
     */
    fun createImageUri(): Uri? {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        currentPhotoUri = photoFile?.let {
             FileProvider.getUriForFile(
                context,
                "com.myolwinoo.universalyoga.admin.fileprovider",
                it
            )
        }
        return currentPhotoUri
    }

    /**
     * Creates a Bitmap from an image URI.
     *
     * @param uri The URI of the image.
     * @return The Bitmap representation of the image.
     */
    fun createBitmap(uri: Uri): Bitmap {
        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
    }

    /**
     * Converts an image URI to a Base64 encoded string.
     *
     * @param imageUri The URI of the image.
     * @return The Base64 encoded string representation of the image, or null if an error occurred.
     */
    fun imageUriToBase64(imageUri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Converts a Base64 encoded string to a Bitmap.
     *
     * @param base64String The Base64 encoded string representation of the image.
     * @return The Bitmap representation of the image, or null if an error occurred.
     */
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Creates a new image file in the application's files directory.
     *
     * @return The newly created image file.
     * @throws IOException If an error occurs while creating the file.
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        val storageDir = context.filesDir.resolve("yoga-images")
        storageDir.mkdirs()
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
}