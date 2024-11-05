@file:OptIn(ExperimentalMaterial3Api::class)

package com.myolwinoo.universalyoga.admin.features

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
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
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val listState = rememberLazyListState()
    val expandedFab = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory())
    val courses = viewModel.courses.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
                onClick = {},
                expanded = expandedFab.value,
                icon = { Icon(painterResource(R.drawable.ic_add), "Create Button") },
                text = { Text(text = "Create") },
            )
        }
    ) { innerPadding ->
        Box {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                items(
                    items = courses.value,
                    key = { it.id }
                ) {
                    CourseItem(course = it)
                }
            }
        }
    }

}

@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: YogaCourse
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseItemPreview() {
    CourseItem(
        course = DummyDataProvider.dummyYogaCourses.first()
    )
}