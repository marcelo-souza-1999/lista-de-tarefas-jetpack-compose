package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.marcelo.souza.listadetarefas.R
import com.patrik.fancycomposedialogs.dialogs.ErrorFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun TaskLogoutConfirmationFancyDialog(
    title: String,
    message: String,
    onConfirmLogout: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ErrorFancyDialog(
        title = title,
        showTitle = true,
        showMessage = true,
        message = message,
        isCancelable = true,
        dialogActionType = DialogActionType.ACTIONABLE,
        dialogProperties = DialogButtonProperties(
            positiveButtonText = R.string.title_button_confirm_logout,
            negativeButtonText = R.string.title_button_cancel_logout,
            buttonColor = MaterialTheme.colorScheme.error,
            buttonTextColor = Color.White
        ),
        dialogStyle = DialogStyle.UPPER_CUTTING,
        positiveButtonClick = onConfirmLogout,
        negativeButtonClick = onCancelClick,
        dismissTouchOutside = onDismissRequest
    )
}
