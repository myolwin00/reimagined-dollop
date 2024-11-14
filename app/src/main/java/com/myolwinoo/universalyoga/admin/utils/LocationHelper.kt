package com.myolwinoo.universalyoga.admin.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

/**
 * Helper class for retrieving the current location of the device.
 *
 * This class uses the Fused Location Provider to get the last known location.
 * It checks for location permissions before attempting to retrieve the location.
 */
class LocationHelper(
    private val context: Context
) {

    /**
     * The Fused Location Provider client.
     */
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Retrieves the current location of the device.
     *
     * This method checks for location permissions before attempting to retrieve the location.
     * If the necessary permissions are not granted, it returns null.
     *
     * @return The current location of the device, or null if location permissions are not granted.
     */
    suspend fun getCurrentLocation(): Location? {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return if (hasFineLocation && hasCoarseLocation) {
            fusedLocationClient.lastLocation.await()
        } else null
    }
}