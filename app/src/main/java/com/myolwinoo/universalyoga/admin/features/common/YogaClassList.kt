package com.myolwinoo.universalyoga.admin.features.common

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.features.course.detail.OnEditClass
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider

fun LazyListScope.yogaClassList(
    yogaClasses: List<YogaClass>,
    onEditClass: OnEditClass,
    onDeleteClass: (classId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit
) {
    itemsIndexed(
        items = yogaClasses,
        key = { i, item -> item.id }
    ) { i, item ->
        if (i != 0) {
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        }
        YogaClassItem(
            modifier = Modifier
                .clickable { onManageClasses(item.courseId) },
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
    onEditClass: OnEditClass,
    onDeleteClass: (classId: String) -> Unit
) {
    Card {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
        ) {
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Icon(
                    modifier = Modifier.padding(top = 4.dp),
                    painter = painterResource(
                        if (yogaClass.teachers.size > 1) {
                            R.drawable.ic_group
                        } else {
                            R.drawable.ic_person
                        }
                    ),
                    contentDescription = "teacher icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = yogaClass.teacherNames,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(
                    modifier = Modifier.padding(top = 4.dp),
                    painter = painterResource(R.drawable.ic_event),
                    contentDescription = "date icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = yogaClass.date,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(
                    modifier = Modifier.padding(top = 4.dp),
                    painter = painterResource(R.drawable.ic_comment),
                    contentDescription = "comment icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = yogaClass.comment.ifBlank { "No comment." },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.End),
            ) {
                FilledTonalButton(
                    onClick = { onDeleteClass(yogaClass.id) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.error,
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "delete button"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Edit")
                }

                FilledTonalButton(onClick = { onEditClass(yogaClass.id, yogaClass.courseId) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "edit button"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Edit")
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun YogaClassItemPreview() {
    UniversalYogaTheme {
        YogaClassItem(
            yogaClass = DummyDataProvider.dummyYogaCourses.first().classes.first(),
            onEditClass = { _, _ -> },
            onDeleteClass = {}
        )
    }
}