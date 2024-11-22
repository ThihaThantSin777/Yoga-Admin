@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.aungpyaephyo.yoga.admin.features.course.detail

import com.aungpyaephyo.yoga.admin.features.common.CourseInfo
import com.aungpyaephyo.yoga.admin.features.common.DeleteConfirmationDialog
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.data.repo.YogaRepository
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.YogaEventType
import com.aungpyaephyo.yoga.admin.features.common.yogaClassList
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import com.aungpyaephyo.yoga.admin.utils.DummyDataProvider
import kotlinx.serialization.Serializable


typealias OnEditClass = (classId: String, courseId: String) -> Unit

@Serializable
data class CourseDetailRoute(val courseId: String)

fun NavController.navigateToCourseDetail(courseId: String) {
    navigate(CourseDetailRoute(courseId))
}

fun NavGraphBuilder.courseDetailScreen(
    repo: YogaRepository,
    onBack: () -> Unit,
    onCreateClass: (courseId: String) -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onEditClass: OnEditClass,
) {
    composable<CourseDetailRoute> {
        val context = LocalContext.current
        val route = it.toRoute<CourseDetailRoute>()
        val viewModel: CourseDetailViewModel =
            viewModel(factory = CourseDetailViewModel.Factory(route.courseId, repo))
        val yogaCourse = viewModel.course.collectAsStateWithLifecycle(null)
        Screen(
            course = yogaCourse.value,
            onBack = onBack,
            onCreateClass = { onCreateClass(route.courseId) },
            onEditClass = onEditClass,

            onDelete = viewModel::deleteClass,
            showConfirmDeleteId = viewModel.confirmDeleteId.value,
            onShowConfirmDelete = viewModel::showConfirmDelete,
            onHideConfirmDelete = viewModel::hideConfirmDelete,
            onEditCourse = onEditCourse,
            onViewLink = { url ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = url.toUri()
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            },
            onOpenMap =
            { lat, long ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    "geo:$lat,$long".toUri()
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    course: YogaCourse?,
    onBack: () -> Unit,
    onCreateClass: () -> Unit,
    onEditClass: OnEditClass,
    onDelete: (classId: String) -> Unit,
    showConfirmDeleteId: String?,
    onShowConfirmDelete: (classId: String) -> Unit,
    onHideConfirmDelete: () -> Unit,
    onEditCourse: (courseId: String) -> Unit,
    onViewLink: (String) -> Unit,
    onOpenMap: (lat: Double, long: Double) -> Unit
) {

    val listState = rememberLazyListState()
    val expandedFab = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(

                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back button"
                        )
                    }
                },
                title = { Text("Yoga Course Detail") },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateClass,
                expanded = expandedFab.value,
                icon = { Icon(painterResource(R.drawable.ic_add), "Create Button") },
                text = { Text(text = "Create Class") },
            )

        }

    )
    { innerPadding ->

        DeleteConfirmationDialog(
            onDelete = onDelete,
            showConfirmDeleteId = showConfirmDeleteId,
            onHideConfirmDelete = onHideConfirmDelete
        )

        course?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)

            ) {
                LazyColumn(

                    modifier = Modifier.padding(top = 16.dp),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding() + 100.dp
                    )
                ) {
                    item {
                        CourseDetail(
                            course = course,
                            onViewLink = onViewLink,
                            onOpenMap = onOpenMap
                        )
                    }
                    item {
                        Text(
                            text = if (course.classes.isEmpty()) {
                                "No Classes Yet"
                            } else {
                                "Classes (${course.classes.size})"
                            },
                            color = MaterialTheme.colorScheme.primary, // Use a primary color for better visibility
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)) // Add a subtle background
                                .padding(vertical = 16.dp), // Simplified vertical padding
                            style = MaterialTheme.typography.headlineSmall.copy( // Use a larger, bolder typography
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp // Add slight letter spacing for emphasis
                            ),
                            textAlign = TextAlign.Center // Center-align the text
                        )

                    }
                    yogaClassList(
                        yogaClasses = course.classes,
                        onEditClass = onEditClass,
                        onDeleteClass = onShowConfirmDelete,
                        onManageClasses = {}
                    )
                }

            }

        }
    }

}


@Composable
fun CourseDetail(
    modifier: Modifier = Modifier,
    course: YogaCourse,
    onViewLink: (String) -> Unit,
    onOpenMap: (lat: Double, long: Double) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            color = Color.Black,
           // modifier = Modifier.fillMaxWidth(),
            text = course.typeOfClass.displayName,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.size(8.dp))

        CourseInfo(course = course)

        Spacer(Modifier.size(8.dp))

        Text(
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            text = "Description: " + course.description.ifBlank { "No description." },
            style = MaterialTheme.typography.bodyLarge

        )

        Spacer(Modifier.size(8.dp))


        Text(
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            text ="Level: " + course.difficultyLevel.name,
        )

        Spacer(Modifier.size(8.dp))
        Column {
            Text(
                fontWeight = FontWeight.Bold,
                text = "Terms & Policy",
                color = Color.Black)

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = "Refunds will not be provided for cancellations made less than 24 hours before the class begins. We appreciate your understanding, as this policy helps maintain fair access to classes for everyone.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }


        Spacer(Modifier.size(20.dp))

        HorizontalDivider()
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    UniversalYogaTheme {
        Screen(
            course = DummyDataProvider.dummyYogaCourses.first(),
            onBack = {},
            onCreateClass = {},
            onEditClass = { _, _ -> },
            onDelete = {},
            showConfirmDeleteId = null,
            onShowConfirmDelete = {},
            onHideConfirmDelete = {},
            onEditCourse = {},
            onViewLink = {},
            onOpenMap = { _, _ -> }
        )
    }
}