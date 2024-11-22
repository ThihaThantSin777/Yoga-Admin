@file:OptIn(ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.common
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import com.aungpyaephyo.yoga.admin.utils.DummyDataProvider



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
        CourseItemView(
            course = it,
            expandClasses = expandClasses,
            onEditCourse = onEditCourse,
            onManageClasses = onManageClasses,
            onDelete = onDelete
        )
    }
}

@Composable
private fun CourseItemView(
    modifier: Modifier = Modifier,
    course: YogaCourse,
    expandClasses: Boolean = false,
    onEditCourse: (courseId: String) -> Unit,
    onManageClasses: (courseId: String) -> Unit,
    onDelete: (courseId: String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, // Ensures space between items
                modifier = Modifier
                    .fillMaxWidth() // Ensures the Row takes up full width
                    .padding(horizontal = 15.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onEditCourse(course.id) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        tint = MaterialTheme.colorScheme.primary, // Adjust tint if necessary
                        contentDescription = "edit button"
                    )
                }
                Text(
                    color = Color.Black,
                    fontSize = 21.sp,
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


            Spacer(Modifier.size(12.dp))

            CourseInfo(
                modifier = Modifier.padding(horizontal = 20.dp),
                course = course
            )

            Spacer(Modifier.size(12.dp))

            Text(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = course.description.ifBlank { "No description." },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            YogaClasses(
                modifier = Modifier
                    .padding(top = 12.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                yogaClasses = course.classes,
                expandClasses = expandClasses,
                onSeeMore = { onManageClasses(course.id) }
            )
            OutlinedButton(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.
                        fillMaxWidth(),
                onClick = { onManageClasses(course.id) }
            ) {
                Text(
                    text = "Manage",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Add a Divider at the bottom of the Card
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp
        )
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
            Spacer(Modifier.size(12.dp))
            Text(
                text = "No classes are available at the moment.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.error, // Set text color to warning (red)
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CourseItemPreview() {
    UniversalYogaTheme {
        CourseItemView(
            course = DummyDataProvider.dummyYogaCourses.first(),
            onEditCourse = {},
            onManageClasses = {},
            onDelete = {}
        )
    }
}