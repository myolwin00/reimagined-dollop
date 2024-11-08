@file:OptIn(ExperimentalMaterial3Api::class)

package com.myolwinoo.universalyoga.admin.features.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.common.courseList
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen(
    repo: YogaRepository,
    onNavigateToSearch: () -> Unit,
    onCreateCourseClick: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit
) {
    composable<HomeRoute> {
        val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repo))
        val courses = viewModel.courses.collectAsStateWithLifecycle(emptyList())
        Screen(
            courses = courses.value,
            onCreateCourseClick = onCreateCourseClick,
            onEditCourse = onEditCourse,
            onManageClasses = onManageClasses,
            onNavigateToSearch = onNavigateToSearch
        )
    }
}

@Composable
private fun Screen(
    courses: List<YogaCourse>,
    onCreateCourseClick: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit
) {

    val listState = rememberLazyListState()
    val expandedFab = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                ),
                title = {
                    Text("All Courses")
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_upload),
                            contentDescription = "upload button"
                        )
                    }
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "search button"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateCourseClick,
                expanded = expandedFab.value,
                icon = { Icon(painterResource(R.drawable.ic_add), "Create Button") },
                text = { Text(text = "Create") },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                courseList(
                    courses = courses,
                    onEditCourse = onEditCourse,
                    onManageClasses = onManageClasses
                )
            }

            if (courses.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "No course available! Start by creating one."
                )
            }
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    UniversalYogaTheme {
        Screen(
            courses = DummyDataProvider.dummyYogaCourses,
            onCreateCourseClick = {},
            onEditCourse = {},
            onManageClasses = {},
            onNavigateToSearch = {}
        )
    }
}