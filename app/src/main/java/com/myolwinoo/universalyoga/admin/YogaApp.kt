package com.myolwinoo.universalyoga.admin

import android.app.Application
import com.myolwinoo.universalyoga.admin.data.db.YogaDatabase

class YogaApp: Application() {

    val yogaDb by lazy {
        YogaDatabase.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}