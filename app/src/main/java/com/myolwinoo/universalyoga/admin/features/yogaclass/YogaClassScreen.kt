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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.common.DeleteConfirmation
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable

@Serializable
data class YogaClassRoute(val courseId: String)

fun NavController.navigateToYogaClass(courseId: String) {
    navigate(YogaClassRoute(courseId))
}

fun NavGraphBuilder.yogaClassScreen(
    repo: YogaRepository,
    onBack: () -> Unit
) {
    composable<YogaClassRoute> {
        val route = it.toRoute<YogaClassRoute>()
        val viewModel: YogaClassViewModel =
            viewModel(factory = YogaClassViewModel.Factory(route.courseId, repo))
        val yogaCourse = viewModel.course.collectAsStateWithLifecycle(null)
        Screen(
            course = yogaCourse.value,
            onBack = onBack,
            onCreateClass = viewModel::createClass,

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
                                text = course.description,
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
                                    Text(text = course.dayOfWeek.displayName)
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
                                    text = "Schedules (${course.classes.size})",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            yogaClassList(
                                yogaClasses = course.classes,
                                onEditClass = {},
                                onDeleteClass = onShowConfirmDelete
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

private fun LazyListScope.yogaClassList(
    yogaClasses: List<YogaClass>,
    onEditClass: (classId: String) -> Unit,
    onDeleteClass: (classId: String) -> Unit
) {
    itemsIndexed(
        items = yogaClasses,
        key = { i, item -> item.id }
    ) { i, item ->
        if (i != 0) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        }
        YogaClassItem(
            yogaClass = item,
            onEditClass = onEditClass,
            onDeleteClass = onDeleteClass
        )
    }
}

@Composable
private fun YogaClassItem(
    modifier: Modifier = Modifier,
    yogaClass: YogaClass,
    onEditClass: (classId: String) -> Unit,
    onDeleteClass: (classId: String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = modifier.fillMaxWidth()) {
            Column(modifier = modifier.weight(1f)) {
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_person),
                        contentDescription = "people icon"
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = yogaClass.teacherName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_event),
                        contentDescription = "date icon"
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = yogaClass.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            IconButton(
                modifier = Modifier.size(36.dp),
                onClick = { onEditClass(yogaClass.id) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "edit button"
                )
            }

            IconButton(
                modifier = Modifier.size(36.dp),
                onClick = { onDeleteClass(yogaClass.id) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "delete button"
                )
            }
        }

        Row(modifier = Modifier.padding(top = 4.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = "comment icon"
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = yogaClass.comment.ifBlank { "No comment." },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun YogaClassItemPreview() {
    UniversalYogaTheme {
        YogaClassItem(
            yogaClass = DummyDataProvider.dummyYogaCourses.first().classes.first(),
            onEditClass = {},
            onDeleteClass = {}
        )
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
            onDelete = {},
            showConfirmDeleteId = null,
            onShowConfirmDelete = {},
            onHideConfirmDelete = {}
        )
    }
}