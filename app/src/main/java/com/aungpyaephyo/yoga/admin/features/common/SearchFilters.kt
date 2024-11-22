@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.features.common.convertMillisToDate
import com.aungpyaephyo.yoga.admin.features.create.DayOfWeekSelector
import com.aungpyaephyo.yoga.admin.theme.UniversalYogaTheme

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilters(
    modifier: Modifier = Modifier,
    dateFilter: String? = null,
    onDateFilterChange: (String?) -> Unit,
    dayFilter: DayOfWeek? = null,
    onDayFilterChange: (DayOfWeek?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showDayPicker by remember { mutableStateOf(false) }
    var showRemoveDateFilterConfirmation by remember { mutableStateOf(false) }
    var showRemoveDayFilterConfirmation by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        convertMillisToDate(it)
                    }.orEmpty()
                    onDateFilterChange(selectedDate)
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showDayPicker) {
        DayPicker(
            initialDay = dayFilter ?: DayOfWeek.MONDAY,
            onConfirm = { onDayFilterChange(it) },
            onDismiss = { showDayPicker = false }
        )
    }

    if (showRemoveDateFilterConfirmation) {
        ConfirmRemoveFilter(
            onConfirm = { onDateFilterChange(null) },
            onDismiss = { showRemoveDateFilterConfirmation = false }
        )
    }

    if (showRemoveDayFilterConfirmation) {
        ConfirmRemoveFilter(
            onConfirm = { onDayFilterChange(null) },
            onDismiss = { showRemoveDayFilterConfirmation = false }
        )
    }

    Row(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dateFilter?.let {
            InputChip(
                colors = InputChipDefaults.inputChipColors(
                    containerColor = (MaterialTheme.colorScheme.primary), // Background color
                    labelColor = Color.White, // Label (text) color
                    leadingIconColor = MaterialTheme.colorScheme.primary, // Leading icon color
                    //trailingIconColor = Color.White // Trailing icon color
                ),

                    // colors = InputChipDefault.InputChipColor(),
                onClick = { showRemoveDateFilterConfirmation = true },
                label = { Text(dateFilter, color = Color.Black) },
                selected = true,
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.ic_clear),
                        contentDescription = "close button",
                        Modifier.size(20.dp)
                    )
                },
            )
        } ?: run {
            AssistChip(
                onClick = { showDatePicker = true },
                label = { Text("Choose Date", color = Color.Black) },
            )
        }

        dayFilter?.let {
            InputChip(
                onClick = { showRemoveDayFilterConfirmation = true },
                label = {
                    Text(
                        dayFilter.getDisplayName(
                            TextStyle.FULL_STANDALONE,
                            Locale.getDefault()
                        )
                    )
                },
                selected = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = "close button",
                        modifier = Modifier.size(20.dp)
                    )
                },
            )
        } ?: run {
            AssistChip(
                onClick = { showDayPicker = true },
                label = { Text("Choose Day") },
            )
        }
    }
}

@Composable
private fun DayPicker(
    initialDay: DayOfWeek,
    onConfirm: (DayOfWeek) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedDayOfWeek by remember { mutableStateOf(initialDay) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Select Day",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DayOfWeekSelector(
                    selectedDayOfWeek = selectedDayOfWeek,
                    onDayOfWeekSelected = {
                        selectedDayOfWeek = it
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onConfirm(selectedDayOfWeek)
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfirmRemoveFilter(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("OK")
            }
        },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = "Remove Filter"
            )
        },
        title = {
            Text("Remove Filter")
        },
        text = {
            Text("Are you sure you want to remove this filter?")
        }
    )
}

@Preview
@Composable
private fun SearchFiltersPreviewEnabled() {
    UniversalYogaTheme {
        SearchFilters(
            dateFilter = null,
            onDateFilterChange = {},
            dayFilter = null,
            onDayFilterChange = {}
        )
    }
}

@Preview
@Composable
private fun SearchFiltersPreviewDisabled() {
    UniversalYogaTheme {
        SearchFilters(
            dateFilter = "12/10/2024",
            onDateFilterChange = {},
            dayFilter = DayOfWeek.MONDAY,
            onDayFilterChange = {}
        )
    }
}