package com.example.searchmovieapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.searchmovieapp.ui.theme.SearchMovieAppTheme

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    data: SnackbarData,
    maxLines: Int = 1,
    actionOnNewLine: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {

    val actionLabel = data.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? =
        if (actionLabel != null) {
            @Composable {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                    onClick = { data.performAction() },
                    content = { Text(actionLabel) }
                )
            }
        } else {
            null
        }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (data.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { data.dismiss() },
                    content = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null,
                        )
                    }
                )
            }
        } else {
            null
        }
    Snackbar(
        modifier = modifier.padding(12.dp),
        action = actionComposable,
        dismissAction = dismissActionComposable,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
    ) {
        Text(
            text = data.visuals.message,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CustomSnackBarPreview() {
    SearchMovieAppTheme {
        CustomSnackBar(data = MockSnackBarData())
    }
}

class MockSnackBarData() : SnackbarData {
    override val visuals: SnackbarVisuals
        get() = MockSnackBarVisuals()

    override fun dismiss() {

    }

    override fun performAction() {
    }

}

class MockSnackBarVisuals(
    override val actionLabel: String? = "actionLabelMocker",
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val message: String = "SnackBar test using MockSnackBarData puedo escribir mas cosas y no hay problemas porque al final lo va a cortar",
    override val withDismissAction: Boolean = true
) : SnackbarVisuals