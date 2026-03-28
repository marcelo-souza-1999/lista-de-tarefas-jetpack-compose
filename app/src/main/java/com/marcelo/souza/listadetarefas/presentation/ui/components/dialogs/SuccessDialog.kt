package com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.patrik.fancycomposedialogs.dialogs.SuccessFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import com.patrik.fancycomposedialogs.enums.DialogStyle
import com.patrik.fancycomposedialogs.properties.DialogButtonProperties

@Composable
fun TaskSuccessFancyDialog(
    title: String,
    message: String,
    buttonTextRes: Int = R.string.title_button_success_dialog_registration_task,
    isCancelable: Boolean = true,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    SuccessFancyDialog(
        title = title,
        showTitle = true,
        showMessage = true,
        message = message,
        isCancelable = isCancelable,
        dialogActionType = DialogActionType.INFORMATIVE,
        dialogProperties = DialogButtonProperties(
            neutralButtonText = buttonTextRes,
            buttonColor = MaterialTheme.colorScheme.primary,
            buttonTextColor = Color.White
        ),
        dialogStyle = DialogStyle.UPPER_CUTTING,
        neutralButtonClick = {
            onConfirmClick()
            onDismissRequest()
        },
        dismissTouchOutside = onDismissRequest
    )
}

@Preview(showBackground = true)
@Composable
internal fun TaskSuccessFancyDialogPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        TaskSuccessFancyDialog(
            title = "Sucesso!",
            message = "A tarefa foi salva com sucesso.",
            buttonTextRes = R.string.title_button_success_dialog_registration_task,
            isCancelable = true,
            onConfirmClick = {},
            onDismissRequest = {}
        )
    }
}