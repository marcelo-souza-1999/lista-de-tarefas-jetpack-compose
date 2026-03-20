package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.patrik.fancycomposedialogs.dialogs.ErrorFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun TaskErrorFancyDialog(
    title: String,
    message: String,
    isCancelable: Boolean = true,
    onRetryClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ErrorFancyDialog(
        title = title,
        showTitle = true,
        showMessage = true,
        message = message,
        isCancelable = isCancelable,
        dialogActionType = DialogActionType.ACTIONABLE,
        dialogProperties = DialogButtonProperties(
            positiveButtonText = R.string.title_button_positive_error_dialog_registration_task,
            negativeButtonText = R.string.title_button_negative_error_dialog_registration_task,
            buttonColor = MaterialTheme.colorScheme.error,
            buttonTextColor = Color.White
        ),
        dialogStyle = DialogStyle.UPPER_CUTTING,
        positiveButtonClick = onRetryClick,
        negativeButtonClick = onCancelClick,
        dismissTouchOutside = onDismissRequest
    )
}

@Preview(showBackground = true)
@Composable
internal fun TaskErrorFancyDialogPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        TaskErrorFancyDialog(
            title = "Ops, algo deu errado!",
            message = "Não foi possível salvar sua tarefa. Tente novamente.",
            onRetryClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        )
    }
}
