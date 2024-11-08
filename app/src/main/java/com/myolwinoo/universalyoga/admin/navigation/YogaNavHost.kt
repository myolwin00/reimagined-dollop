package com.myolwinoo.universalyoga.admin.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.create.createCourseScreen
import com.myolwinoo.universalyoga.admin.features.create.navigateToCreateCourse
import com.myolwinoo.universalyoga.admin.features.home.HomeRoute
import com.myolwinoo.universalyoga.admin.features.home.homeScreen
import com.myolwinoo.universalyoga.admin.features.search.navigateToSearch
import com.myolwinoo.universalyoga.admin.features.search.searchScreen
import com.myolwinoo.universalyoga.admin.features.yogaclass.navigateToYogaClass
import com.myolwinoo.universalyoga.admin.features.yogaclass.yogaClassScreen

private const val TIME_DURATION = 300

@Composable
fun YogaNavHost(
    repo: YogaRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
            )
        }
    ) {

        homeScreen(
            repo = repo,
            onCreateCourseClick = navController::navigateToCreateCourse,
            onEditCourse = {},
            onManageClasses = navController::navigateToYogaClass,
            onNavigateToSearch = navController::navigateToSearch
        )

        searchScreen(
            repo = repo,
            onBack = navController::popBackStack,
            onEditCourse = {},
            onManageClasses = navController::navigateToYogaClass
        )

        createCourseScreen(
            repo = repo,
            onBack = navController::popBackStack
        )

        yogaClassScreen(
            repo = repo,
            onBack = navController::popBackStack
        )
    }
}