@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.myolwinoo.universalyoga.admin.features.course.detail

import android.content.Intent
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.CancellationPolicy
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.model.YogaEventType
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.common.CourseInfo
import com.myolwinoo.universalyoga.admin.features.common.DeleteConfirmation
import com.myolwinoo.universalyoga.admin.features.common.YogaImageList
import com.myolwinoo.universalyoga.admin.features.common.yogaClassList
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable

typealias OnEditClass = (classId: String, courseId: String) -> Unit

@Serializable
data class CourseDetailRoute(val courseId: String)

fun NavController.navigateToCourseDetail(courseId: String) {
    navigate(CourseDetailRoute(courseId))
}

fun NavGraphBuilder.courseDetailScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
    onCreateClass: (courseId: String) -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onEditClass: OnEditClass,
) {
    composable<CourseDetailRoute> {
        val context = LocalContext.current
        val route = it.toRoute<CourseDetailRoute>()
        val viewModel: CourseDetailViewModel =
            viewModel(factory = CourseDetailViewModel.Factory(route.courseId, repo))
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
            onEditCourse = onEditCourse,
            onViewLink = { url ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = url.toUri()
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            },
            onOpenMap = { lat, long ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    "geo:$lat,$long".toUri()
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
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
    onEditCourse: (courseId: String) -> Unit,
    onViewLink: (String) -> Unit,
    onOpenMap: (lat: Double, long: Double) -> Unit
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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back button"
                        )
                    }
                },
                title = { Text("Course Detail") },
                actions = {
                    course?.let {
                        OutlinedButton(onClick = {
                            onEditCourse(course.id)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = "Edit course")
                        }
                    }
                }
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
                    item {
                        CourseDetail(
                            course = course,
                            onViewLink = onViewLink,
                            onOpenMap = onOpenMap
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(top = 20.dp, bottom = 8.dp),
                            text = if (course.classes.isEmpty()) {
                                "No classes yet."
                            } else {
                                "Classes (${course.classes.size})"
                            },
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
    }
}

@Composable
fun CourseDetail(
    modifier: Modifier = Modifier,
    course: YogaCourse,
    onViewLink: (String) -> Unit,
    onOpenMap: (lat: Double, long: Double) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = course.typeOfClass.displayName,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.size(8.dp))

        CourseInfo(course = course)

        Spacer(Modifier.size(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = course.description.ifBlank { "No description." },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.size(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                append("This course is designed for ")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )) {
                    append(course.difficultyLevel.displayName)
                }
                append(" level and the target audience is ")
                withStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )) {
                    append(course.targetAudience.displayName)
                }
                append(".")
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.size(8.dp))
        Text(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .fillMaxWidth(),
            text = when(course.cancellationPolicy) {
                CancellationPolicy.FLEXIBLE -> "Get a full refund if you cancel at least 24 hours before the class starts. Enjoy peace of mind with our flexible cancellation policy."
                CancellationPolicy.MODERATE -> "Receive a 50% refund if you cancel at least 24 hours before the class starts. We offer a moderate cancellation policy to balance flexibility and commitment."
                CancellationPolicy.STRICT -> "No refunds are issued for cancellations made within 24 hours of the class start time. Please plan accordingly as our strict cancellation policy ensures class availability for all."
                CancellationPolicy.NO_REFUND -> "All sales are final, and no refunds will be issued for cancellations. Please be certain of your commitment before booking as this policy ensures fairness to all participants."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        when(course.eventType) {
            YogaEventType.ONLINE -> {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Join online with the class link.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    OutlinedButton(
                        onClick = { onViewLink(course.onlineUrl) }
                    ) {
                        Text("Join")
                    }
                }
            }
            YogaEventType.IN_PERSON -> {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Click to view the location and get directions.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    OutlinedButton(onClick = { onOpenMap(course.latitude, course.longitude)}) {
                        Text("View Location")
                    }
                }
            }
            YogaEventType.UNSPECIFIED -> {}
        }

        Spacer(Modifier.size(8.dp))

        YogaImageList(
            images = course.images,
            imageSize = 150,
            contentPadding = PaddingValues(horizontal = 0.dp)
        )

        Spacer(Modifier.size(20.dp))

        HorizontalDivider()
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
            onHideConfirmDelete = {},
            onEditCourse = {},
            onViewLink = {},
            onOpenMap = { _, _ -> }
        )
    }
}