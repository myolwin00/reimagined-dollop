package com.myolwinoo.universalyoga.admin

import android.app.Application
import com.myolwinoo.universalyoga.admin.data.db.YogaDatabase
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.usecase.SyncDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class YogaApp: Application() {

    val yogaDb by lazy {
        YogaDatabase.getInstance(this)
    }

    val syncDataUseCase by lazy {
        SyncDataUseCase(YogaRepository(yogaDb.yogaDao()))
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        coroutineScope.launch {
            syncDataUseCase.start()
        }
    }
}