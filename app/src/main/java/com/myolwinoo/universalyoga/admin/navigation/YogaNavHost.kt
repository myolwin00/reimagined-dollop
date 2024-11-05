package com.myolwinoo.universalyoga.admin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.create.CreateCourseRoute
import com.myolwinoo.universalyoga.admin.features.create.createCourseScreen
import com.myolwinoo.universalyoga.admin.features.home.HomeRoute
import com.myolwinoo.universalyoga.admin.features.home.HomeScreen
import com.myolwinoo.universalyoga.admin.features.home.HomeViewModel

@Composable
fun YogaNavHost(
    repo: YogaRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {

        composable<HomeRoute> {
            HomeScreen(
                viewModel = viewModel(factory = HomeViewModel.Factory(repo)),
                onCreateCourseClick = {
                    navController.navigate(CreateCourseRoute)
                }
            )
        }

        createCourseScreen(
            repo = repo,
            onBack = { navController.popBackStack() }
        )
    }
}