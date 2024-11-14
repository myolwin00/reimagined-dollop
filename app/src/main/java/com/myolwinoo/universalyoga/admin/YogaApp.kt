package com.myolwinoo.universalyoga.admin

import android.app.Application
import com.myolwinoo.universalyoga.admin.data.db.YogaDatabase
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.usecase.SyncDataUseCase
import com.myolwinoo.universalyoga.admin.utils.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Custom application class for the Yoga app.
 */
class YogaApp : Application() {

    /**
     * Dependencies required by the app, initialized lazily.
     */
    private val yogaDb by lazy { YogaDatabase.getInstance(this) }

    val imageUtils by lazy { ImageUtils(this) }

    val repo by lazy {
        YogaRepository(
            yogaDao = yogaDb.yogaDao(),
            imageUtils = imageUtils
        )
    }

    val syncDataUseCase by lazy { SyncDataUseCase(repo) }


    /**
     * CoroutineScope for running data synchronization tasks.
     */
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        // starts the data synchronization process when the application is created
        coroutineScope.launch {
            syncDataUseCase.start()
        }
    }
}