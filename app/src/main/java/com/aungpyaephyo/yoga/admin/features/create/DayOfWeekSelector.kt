@file:OptIn(ExperimentalLayoutApi::class)

package com.aungpyaephyo.yoga.admin.features.create
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    selectedDayOfWeek: DayOfWeek,
    onDayOfWeekSelected: (DayOfWeek) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            val selected = selectedDayOfWeek == dayOfWeek

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onDayOfWeekSelected(dayOfWeek) }
            ) {
                RadioButton(
                    selected = selected,
                    onClick = { onDayOfWeekSelected(dayOfWeek) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    color = Color.Black,
                    text = dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()),
                    modifier = Modifier.padding(start = 4.dp)

                )
            }
        }
    }
}