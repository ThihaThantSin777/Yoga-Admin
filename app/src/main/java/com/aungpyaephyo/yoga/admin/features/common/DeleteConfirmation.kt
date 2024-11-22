package com.aungpyaephyo.yoga.admin.features.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.aungpyaephyo.yoga.admin.R


@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    onDelete: (courseId: String) -> Unit,
    showConfirmDeleteId: String?,
    onHideConfirmDelete: () -> Unit
) {
    showConfirmDeleteId?.let { id ->
        AlertDialog(
            modifier = modifier,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete Icon"
                )
            },
            title = { Text(text = "Confirm Deletion") },
            text = { Text(text = "Do you want to proceed with deleting this? This action is irreversible.") },
            onDismissRequest = { onHideConfirmDelete() },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(id)
                        onHideConfirmDelete()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = onHideConfirmDelete) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
private fun DeleteConfirmationPreview() {
    DeleteConfirmationDialog(
        onDelete = { },
        showConfirmDeleteId = "123",
        onHideConfirmDelete = { }
    )
}
