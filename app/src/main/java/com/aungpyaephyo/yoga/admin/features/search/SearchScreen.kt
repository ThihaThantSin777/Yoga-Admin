@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.aungpyaephyo.yoga.admin.features.search

import com.aungpyaephyo.yoga.admin.features.common.DeleteConfirmationDialog
import SearchFilters
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.aungpyaephyo.yoga.admin.features.common.yogaClassList
import com.aungpyaephyo.yoga.admin.features.course.detail.OnEditClass
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable

import java.time.format.TextStyle
import java.util.Locale

@Serializable
data object SearchRoute

fun NavController.navigateToSearch() {
    navigate(SearchRoute)
}

fun NavGraphBuilder.searchScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
    onEditClass: OnEditClass,
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
            onManageClasses = onManageClasses,
            onDelete = viewModel::deleteYogaClass,
            showConfirmDeleteId = viewModel.confirmDeleteId.value,
            onShowConfirmDelete = viewModel::showConfirmDelete,
            onHideConfirmDelete = viewModel::hideConfirmDelete,
            onEditClass = onEditClass,
            dateFilter = viewModel.dateFilter,
            onDateFilterChange = viewModel::updateDateFilter,
            dayFilter = viewModel.dayFilter,
            onDayFilterChange = viewModel::updateDayFilter
        )
    }
}

@Composable
private fun Screen(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClearQuery: () -> Unit,
    searchResult: List<YogaClass>,
    suggestions: List<String>,
    onSuggestionClicked: (String) -> Unit,
    onBack: () -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (classId: String) -> Unit,
    showConfirmDeleteId: String?,
    onShowConfirmDelete: (classId: String) -> Unit,
    onHideConfirmDelete: () -> Unit,
    onEditClass: OnEditClass,
    dateFilter: String? = null,
    onDateFilterChange: (String?) -> Unit,
    dayFilter: DayOfWeek? = null,
    onDayFilterChange: (DayOfWeek?) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        DeleteConfirmationDialog(
            onDelete = onDelete,
            showConfirmDeleteId = showConfirmDeleteId,
            onHideConfirmDelete = onHideConfirmDelete
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                stickyHeader {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .animateContentSize()
                    ) {
                        Spacer(Modifier.padding(top = innerPadding.calculateTopPadding()))
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
                                    tint = Color.Black,
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
                                shape = RoundedCornerShape(35),
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
                                    color = Color.Black,
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
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                               disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Disabled background color
                                            ),
                                            onClick = { onSuggestionClicked(it) },
                                            label = { Text(it) }
                                        )

                                    }
                                }
                            }
                        }
                        Column {
                            Text(
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp, top = 8.dp),
                                text = "Filters",
                            )
                            SearchFilters(

                                dateFilter = dateFilter,
                                onDateFilterChange = onDateFilterChange,
                                dayFilter = dayFilter,
                                onDayFilterChange = onDayFilterChange
                            )
                        }
                        if (searchResult.isNotEmpty()) {
                            Text(

                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 6.dp),
                                text = "Search Result (${searchResult.size})",
                            )
                        }
                    }
                }
                yogaClassList(
                    yogaClasses = searchResult,
                    onEditClass = onEditClass,
                    onDeleteClass = onShowConfirmDelete,
                    onManageClasses = onManageClasses
                )
            }

            if (searchResult.isEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center),
                    text = if (query.text.isBlank()) {
                        "Find a class by typing the teacher's name."
                    } else {
                        buildNoClassesFoundMessage(
                            query = query,
                            dateFilter = dateFilter,
                            dayFilter = dayFilter
                        )
                    },
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

private fun buildNoClassesFoundMessage(
    query: TextFieldValue,
    dateFilter: String?,
    dayFilter: DayOfWeek?
): String {
    val message = StringBuilder("We couldn't find any classes")

    if (query.text.isNotBlank()) {
        message.append(" matching with the teacher name \"${query.text}\"")
    }

    val filters = mutableListOf<String>()
    if (dateFilter != null) {
        filters.add("date: $dateFilter")
    }
    if (dayFilter != null) {
        filters.add("day: ${dayFilter.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())}")
    }

    if (filters.isNotEmpty()) {
        message.append(" with the following filters: ${filters.joinToString(", ")}")
    }

    message.append(".")

    return message.toString()
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
            onManageClasses = {},
            onDelete = {},
            showConfirmDeleteId = null,
            onShowConfirmDelete = {},
            onHideConfirmDelete = {},
            onEditClass = { _, _ -> },
            dateFilter = null,
            onDateFilterChange = {},
            dayFilter = null,
            onDayFilterChange = {}
        )
    }
}