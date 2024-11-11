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
import com.myolwinoo.universalyoga.admin.features.yogaclass.create.createYogaClassScreen
import com.myolwinoo.universalyoga.admin.features.yogaclass.create.navigateToCreateYogaClass
import com.myolwinoo.universalyoga.admin.features.course.detail.navigateToCourseDetail
import com.myolwinoo.universalyoga.admin.features.course.detail.courseDetailScreen
import com.myolwinoo.universalyoga.admin.usecase.SyncDataUseCase
import com.myolwinoo.universalyoga.admin.utils.ConnectionChecker
import com.myolwinoo.universalyoga.admin.utils.ImageUtils
import com.myolwinoo.universalyoga.admin.utils.LocationHelper

private const val TIME_DURATION = 300

@Composable
fun YogaNavHost(
    repo: YogaRepository,
    syncDataUseCase: SyncDataUseCase,
    connectionChecker: ConnectionChecker,
    imageUtils: ImageUtils,
    locationHelper: LocationHelper
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
            syncDataUseCase = syncDataUseCase,
            connectionChecker = connectionChecker,
            onCreateCourseClick = { navController.navigateToCreateCourse() },
            onEditCourse = { navController.navigateToCreateCourse(it) },
            onManageClasses = navController::navigateToCourseDetail,
            onNavigateToSearch = navController::navigateToSearch,
        )

        searchScreen(
            repo = repo,
            onBack = navController::popBackStack,
            onManageClasses = navController::navigateToCourseDetail,
            onEditClass = { classId, courseId ->
                navController.navigateToCreateYogaClass(
                    classId = classId,
                    courseId = courseId
                )
            }
        )

        createCourseScreen(
            repo = repo,
            imageUtils = imageUtils,
            locationHelper = locationHelper,
            onBack = navController::popBackStack
        )

        courseDetailScreen(
            repo = repo,
            onBack = navController::popBackStack,
            onEditCourse = { navController.navigateToCreateCourse(it) },
            onCreateClass = { navController.navigateToCreateYogaClass(it) },
            onEditClass = { classId, courseId ->
                navController.navigateToCreateYogaClass(
                    classId = classId,
                    courseId = courseId
                )
            }
        )

        createYogaClassScreen(
            repo = repo,
            onBack = navController::popBackStack
        )
    }
}