package com.myolwinoo.universalyoga.admin.features.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import java.time.format.TextStyle
import java.util.Locale

fun LazyListScope.courseList(
    courses: List<YogaCourse>,
    expandClasses: Boolean = false,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (courseId: String) -> Unit,
) {
    items(
        items = courses,
        key = { it.id }
    ) {
        CourseItem(
            course = it,
            expandClasses = expandClasses,
            onEditCourse = onEditCourse,
            onManageClasses = onManageClasses,
            onDelete = onDelete
        )
    }
}

@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: YogaCourse,
    expandClasses: Boolean = false,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (courseId: String) -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .weight(1f),
                    text = course.typeOfClass.displayName,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onDelete(course.id) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "delete button"
                    )
                }
            }

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

            YogaClasses(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                yogaClasses = course.classes,
                expandClasses = expandClasses,
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
private fun YogaClasses(
    modifier: Modifier = Modifier,
    expandClasses: Boolean,
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
            var expanded by remember { mutableStateOf(expandClasses) }
            Row(
                modifier = Modifier.clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (expanded) "Hide Classes (${yogaClasses.size})" else "Show Classes (${yogaClasses.size})",
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
            onManageClasses = {},
            onDelete = {}
        )
    }
}