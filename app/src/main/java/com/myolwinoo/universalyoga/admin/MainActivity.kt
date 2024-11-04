package com.myolwinoo.universalyoga.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.myolwinoo.universalyoga.admin.features.HomeScreen
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniversalYogaTheme {
                HomeScreen()
                YogaApp()
            }
        }
    }
}