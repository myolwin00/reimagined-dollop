@file:OptIn(ExperimentalMaterial3Api::class)

package com.myolwinoo.universalyoga.admin.features.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.myolwinoo.universalyoga.admin.features.common.DeleteConfirmation
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
            onNavigateToSearch = onNavigateToSearch,

            onDelete = viewModel::deleteCourse,
            showConfirmDeleteId = viewModel.confirmDeleteId.value,
            onShowConfirmDelete = viewModel::showConfirmDelete,
            onHideConfirmDelete = viewModel::hideConfirmDelete,

            onUpload = viewModel::uploadDataToServer,
            showConfirmUpload = viewModel.confirmUpload.value,
            onShowConfirmUpload = viewModel::showConfirmUpload,
            onHideConfirmUpload = viewModel::hideConfirmUpload
        )
    }
}

@Composable
private fun Screen(
    courses: List<YogaCourse>,
    onCreateCourseClick: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (courseId: String) -> Unit,
    showConfirmDeleteId: String?,
    onShowConfirmDelete: (courseId: String) -> Unit,
    onHideConfirmDelete: () -> Unit,

    onUpload: () -> Unit,
    showConfirmUpload: Boolean,
    onShowConfirmUpload: () -> Unit,
    onHideConfirmUpload: () -> Unit
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
                    Text("All Courses" + if (courses.isEmpty()) "" else " (${courses.size})")
                },
                actions = {
                    IconButton(onClick = onShowConfirmUpload) {
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

        DeleteConfirmation(
            onDelete = onDelete,
            showConfirmDeleteId = showConfirmDeleteId,
            onHideConfirmDelete = onHideConfirmDelete
        )

        UploadConfirmation(
            onUpload = onUpload,
            showConfirmUpload = showConfirmUpload,
            onHideConfirmUpload = onHideConfirmUpload
        )

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
                    onManageClasses = onManageClasses,
                    onDelete = onShowConfirmDelete
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

@Composable
private fun UploadConfirmation(
    modifier: Modifier = Modifier,
    onUpload: () -> Unit,
    showConfirmUpload: Boolean,
    onHideConfirmUpload: () -> Unit
) {
    if (showConfirmUpload) {
        AlertDialog(
            modifier = modifier,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = "Upload Icon"
                )
            },
            title = { Text(text = "Upload to Server") },
            text = { Text(text = "Uploading this data will replace any existing data on the server. Are you sure you want to proceed?") },
            onDismissRequest = { onHideConfirmUpload() },
            confirmButton = {
                Button(
                    onClick = {
                        onUpload()
                        onHideConfirmUpload()
                    }
                ) {
                    Text("Upload")
                }
            },
            dismissButton = {
                TextButton(onClick = onHideConfirmUpload) {
                    Text("Cancel")
                }
            }
        )
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
            onNavigateToSearch = {},
            onDelete = {},
            showConfirmDeleteId = "123",
            onShowConfirmDelete = {},
            onHideConfirmDelete = {},
            onUpload = {},
            showConfirmUpload = true,
            onShowConfirmUpload = {},
            onHideConfirmUpload = {}
        )
    }
}