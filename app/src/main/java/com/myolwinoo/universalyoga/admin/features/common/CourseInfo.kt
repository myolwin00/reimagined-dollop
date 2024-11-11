@file:OptIn(ExperimentalLayoutApi::class)

package com.myolwinoo.universalyoga.admin.features.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import com.myolwinoo.universalyoga.admin.utils.DummyDataProvider
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CourseInfo(
    modifier: Modifier = Modifier,
    course: YogaCourse
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoItem(
            iconRes = R.drawable.ic_event,
            text = course.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        )

        InfoItem(
            iconRes = R.drawable.ic_time,
            text = course.time
        )

        InfoItem(
            iconRes = R.drawable.ic_duration,
            text = course.duration
        )

        InfoItem(
            iconRes = R.drawable.ic_location,
            text = course.eventType.displayName
        )

        InfoItem(
            iconRes = R.drawable.ic_group,
            text = course.capacity.toString()
        )

        InfoItem(
            iconRes = R.drawable.ic_euro,
            text = course.displayPrice
        )
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    text: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            contentDescription = null
        )
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseInfoPreview() {
    UniversalYogaTheme {
        CourseInfo(
            course = DummyDataProvider.dummyYogaCourses.first()
        )
    }
}