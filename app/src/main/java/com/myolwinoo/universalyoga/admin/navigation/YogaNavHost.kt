package com.myolwinoo.universalyoga.admin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.create.createCourseScreen
import com.myolwinoo.universalyoga.admin.features.create.navigateToCreateCourse
import com.myolwinoo.universalyoga.admin.features.home.HomeRoute
import com.myolwinoo.universalyoga.admin.features.home.homeScreen

@Composable
fun YogaNavHost(
    repo: YogaRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {

        homeScreen(
            repo = repo,
            onCreateCourseClick = navController::navigateToCreateCourse
        )

        createCourseScreen(
            repo = repo,
            onBack = navController::popBackStack
        )
    }
}