package com.marcelo.souza.listadetarefas.presentation.snapshot.components.dialogs

import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskErrorFancyDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskErrorFancyDialogSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun errorDialog_snapshot_light() {
        runErrorDialogSnapshot(
            darkTheme = false,
            name = "dialog_error_light"
        )
    }

    @Test
    fun errorDialog_snapshot_dark() {
        runErrorDialogSnapshot(
            darkTheme = true,
            name = "dialog_error_dark"
        )
    }

    @Test
    fun errorDialog_snapshot_long_error_message() {
        runErrorDialogSnapshot(
            title = "Falha na Conexão",
            message = "Ocorreu um erro inesperado ao tentar sincronizar seus dados com o servidor. " +
                "Por favor, verifique sua conexão com a internet e tente novamente mais tarde.",
            darkTheme = false,
            name = "dialog_error_long_message_light"
        )
    }

    private fun runErrorDialogSnapshot(
        title: String = "Ops, algo deu errado!",
        message: String = "Não foi possível completar a ação.",
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskErrorFancyDialog(
                    title = title,
                    message = message,
                    onRetryClick = {},
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeRule.waitForIdle()

        val dialogNode = composeRule.onNode(isDialog())

        compareScreenshot(node = dialogNode, name = name)
    }
}
