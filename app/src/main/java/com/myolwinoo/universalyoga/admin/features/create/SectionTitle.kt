package com.myolwinoo.universalyoga.admin.features.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp),
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun SectionTitlePreview() {
    SectionTitle(title = "Test")
}

@Composable
fun SectionSubtitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium,
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}