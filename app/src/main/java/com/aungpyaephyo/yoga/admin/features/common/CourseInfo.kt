@file:OptIn(ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.common


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.data.model.YogaCourse
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme
import com.aungpyaephyo.yoga.admin.utils.DummyDataProvider



import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourseInfo(
    modifier: Modifier = Modifier.padding(end = 20.dp),
    course: YogaCourse
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
       horizontalArrangement = Arrangement.spacedBy(19.dp),
       maxItemsInEachRow = 3,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoItem(
            label = "Date:",
            text = course.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        )

        InfoItem(
            label = "Time:",
            text = course.time
        )

        InfoItem(
            label = "Duration:",
            text = course.duration
        )

        InfoItem(
            label = "Type:",
            text = course.eventType.displayName
        )

        InfoItem(
            label = "Capacity:",
            text = course.capacity.toString()
        )

        InfoItem(
            label = "Price:",
            text = course.displayPrice
        )
    }
}

    @Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    label: String,
    text: String
) {
    Row(
        modifier = modifier,
    ) {
        // Label as bold text
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        // Display the value
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
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