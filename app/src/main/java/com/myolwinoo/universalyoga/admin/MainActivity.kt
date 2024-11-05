package com.myolwinoo.universalyoga.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.navigation.YogaNavHost
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = (application as YogaApp).yogaDb
        val repo = YogaRepository(db.yogaDao())

        setContent {
            UniversalYogaTheme {
                YogaNavHost(repo)
            }
        }
    }
}