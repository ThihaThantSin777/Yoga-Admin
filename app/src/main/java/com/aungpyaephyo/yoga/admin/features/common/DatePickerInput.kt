@file:OptIn(ExperimentalMaterial3Api::class)

package com.aungpyaephyo.yoga.admin.features.common
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.aungpyaephyo.yoga.admin.R


import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput(
    modifier: Modifier = Modifier,
    selectableDay: DayOfWeek,
    date: TextFieldValue,
    onDateChange: (TextFieldValue) -> Unit,
    isError: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val interactionSource = remember {
        InputClickInteractionSource(onClick = { showDatePicker = true })
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = date,
            onValueChange = onDateChange,
            label = { Text("Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_event),
                        contentDescription = "Select date"
                    )
                }
            },
            supportingText = {
                Text( "Mandatory* - Classes can only be scheduled on '${selectableDay.getDisplayName( TextStyle.FULL_STANDALONE, Locale.getDefault() )}'." )
            },
            interactionSource = interactionSource,
            isError = isError
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = getInitialSelectedDate(selectableDay),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val localDate = Instant.fromEpochMilliseconds(utcTimeMillis)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                        val today = Clock.System.now().toEpochMilliseconds()
                        return (localDate.dayOfWeek == selectableDay) && (utcTimeMillis > today)
                    }
                }
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedDate = datePickerState.selectedDateMillis?.let {
                            convertMillisToDate(it)
                        }.orEmpty()
                        onDateChange(TextFieldValue(selectedDate))
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
    }
}

private fun getInitialSelectedDate(selectableDay: DayOfWeek): Long {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysUntil = (selectableDay.ordinal - today.dayOfWeek.ordinal + 7) % 7
    return today.plus(daysUntil, DateTimeUnit.DAY)
        .atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}