@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.myolwinoo.universalyoga.admin.features.yogaclass

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.common.DeleteConfirmation
import com.myolwinoo.universalyoga.admin.features.common.yogaClassList
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable
import java.time.format.TextStyle
import java.util.Locale

typealias OnEditClass = (classId: String, courseId: String) -> Unit

@Serializable
data class YogaClassRoute(val courseId: String)

fun NavController.navigateToYogaClass(courseId: String) {
    navigate(YogaClassRoute(courseId))
}

fun NavGraphBuilder.yogaClassScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
    onCreateClass: (courseId: String) -> Unit,
    onEditClass: OnEditClass,
) {
    composable<YogaClassRoute> {
        val route = it.toRoute<YogaClassRoute>()
        val viewModel: YogaClassViewModel =
            viewModel(factory = YogaClassViewModel.Factory(route.courseId, repo))
        val yogaCourse = viewModel.course.collectAsStateWithLifecycle(null)
        Screen(
            course = yogaCourse.value,
            onBack = onBack,
            onCreateClass = { onCreateClass(route.courseId) },
            onEditClass = onEditClass,

            onDelete = viewModel::deleteClass,
            showConfirmDeleteId = viewModel.confirmDeleteId.value,
            onShowConfirmDelete = viewModel::showConfirmDelete,
            onHideConfirmDelete = viewModel::hideConfirmDelete,
        )
    }
}

@Composable
private fun Screen(
    course: YogaCourse?,
    onBack: () -> Unit,
    onCreateClass: () -> Unit,
    onEditClass: OnEditClass,
    onDelete: (classId: String) -> Unit,
    showConfirmDeleteId: String?,
    onShowConfirmDelete: (classId: String) -> Unit,
    onHideConfirmDelete: () -> Unit,
) {

    val listState = rememberLazyListState()
    val expandedFab = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back button"
                        )
                    }
                },
                title = { Text("") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateClass,
                expanded = expandedFab.value,
                icon = { Icon(painterResource(R.drawable.ic_add), "Create Button") },
                text = { Text(text = "Create Class") },
            )
        }
    ) { innerPadding ->

        DeleteConfirmation(
            onDelete = onDelete,
            showConfirmDeleteId = showConfirmDeleteId,
            onHideConfirmDelete = onHideConfirmDelete
        )

        course?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column {
                    Card(
                        shape = RoundedCornerShape(ZeroCornerSize)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                        ) {

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = course.typeOfClass.displayName,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(Modifier.size(8.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = course.description.ifBlank { "No description." },
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(Modifier.size(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_event),
                                        contentDescription = "day icon"
                                    )
                                    Text(text = course.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()))
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_time),
                                        contentDescription = "time icon"
                                    )
                                    Text(text = course.time)
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_group),
                                        contentDescription = "capacity icon"
                                    )
                                    Text(text = course.capacity.toString())
                                }
                            }
                        }
                    }

                    if (course.classes.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.padding(top = 16.dp),
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = innerPadding.calculateBottomPadding() + 100.dp
                            )
                        ) {
                            stickyHeader {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(bottom = 8.dp),
                                    text = "Classes (${course.classes.size})",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            yogaClassList(
                                yogaClasses = course.classes,
                                onEditClass = onEditClass,
                                onDeleteClass = onShowConfirmDelete,
                                onManageClasses = {}
                            )
                        }
                    }
                }

                if (course.classes.isEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "No classes available! Start by creating one."
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    UniversalYogaTheme {
        Screen(
            course = DummyDataProvider.dummyYogaCourses.first(),
            onBack = {},
            onCreateClass = {},
            onEditClass = { _, _ -> },
            onDelete = {},
            showConfirmDeleteId = null,
            onShowConfirmDelete = {},
            onHideConfirmDelete = {}
        )
    }
}