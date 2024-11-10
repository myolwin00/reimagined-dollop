package com.myolwinoo.universalyoga.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.navigation.YogaNavHost
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.ConnectionChecker

class MainActivity : ComponentActivity() {

    private lateinit var connectionChecker: ConnectionChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = (application as YogaApp).yogaDb
        val repo = YogaRepository(db.yogaDao())
        val syncDataUseCase = (application as YogaApp).syncDataUseCase

        connectionChecker = ConnectionChecker(applicationContext)

        setContent {
            UniversalYogaTheme {
                YogaNavHost(
                    repo = repo,
                    syncDataUseCase = syncDataUseCase,
                    connectionChecker = connectionChecker
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connectionChecker.start()
    }

    override fun onStop() {
        super.onStop()
        connectionChecker.stop()
    }
}