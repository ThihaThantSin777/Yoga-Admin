package com.aungpyaephyo.yoga.admin.features.common

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.YogaClass
import com.aungpyaephyo.yoga.admin.features.course.detail.OnEditClass
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import com.aungpyaephyo.yoga.admin.utils.DummyDataProvider


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
        YogaClassItemView(
            modifier = Modifier
                .clickable { onManageClasses(item.courseId) },
            yogaClass = item,
            onEditClass = onEditClass,
            onDeleteClass = onDeleteClass
        )
    }
}

@Composable
private fun YogaClassItemView(
    modifier: Modifier = Modifier,
    yogaClass: YogaClass,
    onEditClass: OnEditClass,
    onDeleteClass: (classId: String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        ) {
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Icon(
                    tint = Color.Black,
                    modifier = Modifier.padding(top = 6.dp, start = 10.dp),
                    painter = painterResource(R.drawable.ic_group),
                    contentDescription = "teacher icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    color = Color.Black,
                    modifier = Modifier.padding(top = 2.dp),
                    text = yogaClass.teacherNames,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(
                    tint = Color.Black,
                    modifier = Modifier.padding(top = 4.dp, start = 10.dp),
                    painter = painterResource(R.drawable.ic_event),
                    contentDescription = "date icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    color = Color.Black,
                    modifier = Modifier.padding(top = 2.dp),
                    text = yogaClass.date,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Icon(
                    tint = Color.Black,
                    modifier = Modifier.padding(top = 4.dp, start = 10.dp),
                    painter = painterResource(R.drawable.ic_comment),
                    contentDescription = "comment icon"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    color = Color.Black,
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
                    Text(text = "Delete")
                }

                FilledTonalButton(
                    onClick = { onEditClass(yogaClass.id, yogaClass.courseId) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    Icon(
                        tint = Color.Black,
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "edit button"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Edit", color = Color.Black)
                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp
        )
    }
}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun YogaClassItemPreview() {
    UniversalYogaTheme {
        YogaClassItemView(
            yogaClass = DummyDataProvider.dummyYogaCourses.first().classes.first(),
            onEditClass = { _, _ -> },
            onDeleteClass = {}
        )
    }
}