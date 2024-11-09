@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.myolwinoo.universalyoga.admin.features.search

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaCourse
import com.myolwinoo.universalyoga.admin.data.repo.YogaRepository
import com.myolwinoo.universalyoga.admin.features.common.courseList
import com.myolwinoo.universalyoga.admin.ui.theme.UniversalYogaTheme
import kotlinx.serialization.Serializable
import kotlin.math.sin

@Serializable
data object SearchRoute

fun NavController.navigateToSearch() {
    navigate(SearchRoute)
}

fun NavGraphBuilder.searchScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit
) {
    composable<SearchRoute> {
        val viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory(repo))
        val searchResult = viewModel.searchResult.collectAsStateWithLifecycle()
        val suggestions = viewModel.suggestions.collectAsStateWithLifecycle()
        Screen(
            query = viewModel.searchQuery.value,
            onQueryChange = { viewModel.updateQuery(it) },
            onClearQuery = { viewModel.updateQuery(TextFieldValue()) },
            searchResult = searchResult.value,
            suggestions = suggestions.value,
            onSuggestionClicked = {
                viewModel.updateQuery(TextFieldValue(it, TextRange(it.length)))
            },
            onBack = onBack,
            onEditCourse = onEditCourse,
            onManageClasses = onManageClasses,
            onDelete = viewModel::deleteCourse,
            showConfirmDeleteId = viewModel.confirmDeleteId.value,
            onShowConfirmDelete = viewModel::showConfirmDelete,
            onHideConfirmDelete = viewModel::hideConfirmDelete
        )
    }
}

@Composable
private fun Screen(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClearQuery: () -> Unit,
    searchResult: List<YogaCourse>,
    suggestions: List<String>,
    onSuggestionClicked: (String) -> Unit,
    onBack: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (courseId: String) -> Unit,
    showConfirmDeleteId: String?,
    onShowConfirmDelete: (courseId: String) -> Unit,
    onHideConfirmDelete: () -> Unit
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        showConfirmDeleteId?.let { id ->
            AlertDialog(
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete Icon"
                    )
                },
                title = { Text(text = "Confirm Delete") },
                text = { Text(text = "Are you sure you want to delete this? This action cannot be undone.") },
                onDismissRequest = { onHideConfirmDelete() },
                confirmButton = {
                    Button(
                        onClick = {
                            onDelete(id)
                            onHideConfirmDelete()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onHideConfirmDelete) {
                        Text("Cancel")
                    }
                }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .animateContentSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = "back button"
                                )
                            }
                            OutlinedTextField(
                                modifier = Modifier.weight(1f),
                                value = query,
                                onValueChange = onQueryChange,
                                maxLines = 1,
                                placeholder = { Text("Search") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                shape = RoundedCornerShape(50),
                                trailingIcon = if (query.text.isNotEmpty()) {
                                    {
                                        IconButton(
                                            onClick = onClearQuery,
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_clear),
                                                contentDescription = "clear button"
                                            )
                                        }
                                    }
                                } else null,
                                singleLine = true,

                            )
                        }
                        AnimatedVisibility(
                            suggestions.isNotEmpty(),
                            enter = slideInHorizontally() + fadeIn(),
                            exit = slideOutHorizontally() + fadeOut()
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp, top = 8.dp),
                                    text = "Suggestions",
                                )
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 20.dp)
                                ) {
                                    items(
                                        items = suggestions,
                                        key = { it }
                                    ) {
                                        SuggestionChip(
                                            onClick = { onSuggestionClicked(it) },
                                            label = { Text(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                courseList(
                    courses = searchResult,
                    expandClasses = true,
                    onEditCourse = onEditCourse,
                    onManageClasses = onManageClasses,
                    onDelete = onDelete
                )
            }

            if (searchResult.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center),
                    text = if (query.text.isBlank()) {
                        "Search for a course by entering a teacher name."
                    } else {
                        "We couldn't find any courses matching with the teacher name \"${query.text}\"."
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    UniversalYogaTheme {
        Screen(
            query = TextFieldValue("Miss Rowena"),
            onClearQuery = {},
            suggestions = listOf("Miss Rowena", "Sir Dumbledore", "Miss Marcie"),
            onSuggestionClicked = {},
            onQueryChange = {},
            searchResult = emptyList(),
            onBack = {},
            onEditCourse = {},
            onManageClasses = {},
            onDelete = {},
            showConfirmDeleteId = null,
            onShowConfirmDelete = {},
            onHideConfirmDelete = {}
        )
    }
}