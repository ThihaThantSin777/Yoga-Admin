@file:OptIn(ExperimentalMaterial3Api::class)

package com.aungpyaephyo.yoga.admin.features.create



import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.aungpyaephyo.yoga.admin.R
import com.aungpyaephyo.yoga.admin.features.common.InputClickInteractionSource


import java.util.Calendar

@Composable
internal fun CourseTimePicker(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onTimeSelected: (TextFieldValue) -> Unit,
    isError: Boolean,
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    val interactionSource = remember {
        InputClickInteractionSource(onClick = { showTimePicker = true })
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            dismissButton = {
                TextButton(onClick = {
                    showTimePicker = false
                }) { Text("Cancel") }
            },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour.toString().padStart(2, '0')
                    val min = timePickerState.minute.toString().padStart(2, '0')
                    onTimeSelected(TextFieldValue("$hour:$min"))
                    showTimePicker = false
                }) { Text("OK") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    OutlinedTextField(
        modifier = modifier
            .clickable { showTimePicker = true },
        readOnly = true,
        value = value,
        onValueChange = {},
        maxLines = 1,
        singleLine = true,
        interactionSource = interactionSource,
        label = { Text("Start Time") },
        supportingText = { Text("Required*") },
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(painterResource(R.drawable.ic_time), contentDescription = "Select Time")
            }
        },
        isError = isError
    )
}