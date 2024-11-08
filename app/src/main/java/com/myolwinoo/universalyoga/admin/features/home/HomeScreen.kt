@file:OptIn(ExperimentalMaterial3Api::class)

package com.myolwinoo.universalyoga.admin.features.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen(
    repo: YogaRepository,
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
            onManageClasses = onManageClasses
        )
    }
}

@Composable
private fun Screen(
    courses: List<YogaCourse>,
    onCreateCourseClick: () -> Unit,
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
                items(
                    items = courses,
                    key = { it.id }
                ) {
                    CourseItem(
                        course = it,
                        onEditCourse = onEditCourse,
                        onManageClasses = onManageClasses
                    )
                }
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
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: YogaCourse,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
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

            YogaClasses(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                yogaClasses = course.classes,
                onSeeMore = { onManageClasses(course.id) }
            )

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onEditCourse(course.id) }
                ) {
                    Text(
                        text = "Edit course",
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { onManageClasses(course.id) }
                ) {
                    Text(
                        text = "Manage classes",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun YogaClasses(
    modifier: Modifier = Modifier,
    yogaClasses: List<YogaClass>,
    onSeeMore: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        if (yogaClasses.isEmpty()) {
            Text(
                text = "No Classes yet.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (expanded) "Hide Classes" else "Show Classes",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        painter = painterResource(if (expanded) R.drawable.ic_up else R.drawable.ic_down),
                        contentDescription = "expand button"
                    )
                }
            }
            if (expanded) {
                Spacer(Modifier.size(8.dp))
            }
            AnimatedVisibility(expanded) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    yogaClasses.take(4).forEach {
                        Text(
                            text = "${it.date} by ${it.teacherName}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    if (yogaClasses.size > 4) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { onSeeMore() }
                            ,
                            text = "See More",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CourseItemPreview() {
    UniversalYogaTheme {
        CourseItem(
            course = DummyDataProvider.dummyYogaCourses.first(),
            onEditCourse = {},
            onManageClasses = {}
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
            onManageClasses = {}
        )
    }
}