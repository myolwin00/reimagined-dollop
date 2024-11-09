package com.myolwinoo.universalyoga.admin.features.common

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
import com.myolwinoo.universalyoga.admin.R

@Composable
fun DeleteConfirmation(
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
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this? This action cannot be undone.") },
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
    DeleteConfirmation(
        onDelete = { },
        showConfirmDeleteId = "123",
        onHideConfirmDelete = { }
    )
}