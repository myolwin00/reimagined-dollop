package com.myolwinoo.universalyoga.admin.features

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory())
    val courses = viewModel.courses.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
    ) {
        LazyColumn {
            items(
                items = courses.value,
                key = { it.id }
            ) {
                CourseItem(course = it)
            }
        }
    }
}

@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: YogaCourse
) {
    Text(text = course.description)
}

@Preview
@Composable
private fun CourseItemPreview() {
    CourseItem(
        course = DummyDataProvider.dummyYogaCourses.first()
    )
}