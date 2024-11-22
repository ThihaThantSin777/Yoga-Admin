@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.yogaclass.create


import CreateYogaClassViewModel
import com.aungpyaephyo.yoga.admin.features.common.DatePickerInput
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable

@Serializable
data class CreateYogaClassRoute(
    val courseId: String,
    val classId: String? = null,
)

fun NavController.navigateToCreateYogaClass(
    courseId: String,
    classId: String? = null,
) {
    navigate(
        CreateYogaClassRoute(
            classId = classId,
            courseId = courseId
        )
    )
}

fun NavGraphBuilder.createYogaClassScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
) {
    composable<CreateYogaClassRoute> {
        val route = it.toRoute<CreateYogaClassRoute>()
        val viewModel: CreateYogaClassViewModel = viewModel(
            factory = CreateYogaClassViewModel.Factory(
                repo = repo,
                classId = route.classId,
                courseId = route.courseId
            )
        )
        val suggestions = viewModel.suggestions.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.navigateBack) {
            if (viewModel.navigateBack) {
                onBack()
            }
        }

        Screen(
            classId = route.classId,
            dayOfWeek = viewModel.dayOfWeek,
            teacherList = viewModel.teacherList,
            suggestions = suggestions.value,
            onBack = onBack,
            onSave = viewModel::createClass,
            comment = viewModel.comment,
            onCommentChange = { viewModel.comment = it },
            date = viewModel.date,
            onDateChange = {
                viewModel.date = it
                viewModel.resetInputError()
                           },
            teacherName = viewModel.teacherName,
            onTeacherNameChange = viewModel::updateTeacherName,
            onSuggestionClicked = { viewModel.updateTeacherName(TextFieldValue(it, TextRange(it.length))) },
            inputError = viewModel.inputError,
            onRemoveTeacher = viewModel::removeTeacher,
            onAddTeacher = viewModel::addTeacher
        )
    }
}

@Composable
private fun Screen(
    classId: String?,
    inputError: ClassInputError?,
    suggestions: List<String>,
    teacherList: List<String>,
    onAddTeacher: () -> Unit,
    onRemoveTeacher: (String) -> Unit,
    dayOfWeek: DayOfWeek,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onSuggestionClicked: (String) -> Unit,
    teacherName: TextFieldValue,
    onTeacherNameChange: (TextFieldValue) -> Unit,
    date: TextFieldValue,
    onDateChange: (TextFieldValue) -> Unit,
    comment: TextFieldValue,
    onCommentChange: (TextFieldValue) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                ),
                title = {
                    Text(classId?.let { "Edit Yoga Class" } ?: "Create Yoga Class")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back button"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
                Column(
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        value = teacherName,
                        onValueChange = onTeacherNameChange,
                        singleLine = true,
                        label = { Text("Teacher Name") },
                        supportingText = { Text("Required*") },
                        isError = inputError == ClassInputError.TeacherName,
                        trailingIcon = {
                            IconButton(onClick = onAddTeacher) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_add_person),
                                    contentDescription = "add teacher"
                                )
                            }
                        }
                    )
                    if (teacherList.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Teachers",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    FlowRow(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        teacherList.forEach {
                            TeacherItem(
                                name = it,
                                onRemove = onRemoveTeacher
                            )
                        }
                    }
                    AnimatedVisibility(
                        suggestions.isNotEmpty(),
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                DatePickerInput(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    date = date,
                    onDateChange = onDateChange,
                    selectableDay = dayOfWeek,
                    isError = inputError == ClassInputError.Date
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    value = comment,
                    onValueChange = onCommentChange,
                    label = { Text("Comment") },
                    minLines = 4
                )
                Spacer(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f))
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 12.dp
                    )
                    .imePadding()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onSave
                ) {
                    Text(
                        text = classId?.let { "Update Yoga" } ?: "Create Yoga",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TeacherItem(
    modifier: Modifier = Modifier,
    name: String,
    onRemove: (String) -> Unit
) {
    InputChip(
        modifier = modifier,
        onClick = { onRemove(name) },
        label = { Text(name) },
        selected = true,
        avatar = {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_clear),
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    UniversalYogaTheme {
        Screen(
            onBack = {},
            onSave = {},
            classId = null,
            teacherName = TextFieldValue("Jon"),
            onTeacherNameChange = {},
            date = TextFieldValue("12/10/2024"),
            onDateChange = {},
            comment = TextFieldValue("Sample Comment."),
            onCommentChange = {},
            dayOfWeek = DayOfWeek.MONDAY,
            suggestions = listOf("Miss Rowena", "Sir Dumbledore", "Miss Marcie"),
            onSuggestionClicked = {},
            inputError = null,
            teacherList = listOf("Jon", "Rowena"),
            onAddTeacher = {},
            onRemoveTeacher = {}
        )
    }
}