package com.myolwinoo.universalyoga.admin.features.common

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaClass
import com.myolwinoo.universalyoga.admin.features.yogaclass.OnEditClass
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider

fun LazyListScope.yogaClassList(
    yogaClasses: List<YogaClass>,
    onEditClass: OnEditClass,
    horizontalItemSpacing: Int = 0,
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
                .padding(horizontal = horizontalItemSpacing.dp)
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
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
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
                onClick = { onEditClass(yogaClass.id, yogaClass.courseId) }
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
            onEditClass = { _, _ -> },
            onDeleteClass = {}
        )
    }
}