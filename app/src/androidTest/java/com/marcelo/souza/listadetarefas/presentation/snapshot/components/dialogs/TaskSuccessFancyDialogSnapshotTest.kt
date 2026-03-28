package com.marcelo.souza.listadetarefas.presentation.snapshot.components.dialogs

import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskSuccessFancyDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskSuccessFancyDialogSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun taskSuccessDialog_snapshot_light() {
        runSuccessDialogSnapshot(
            darkTheme = false,
            name = "dialog_success_light"
        )
    }

    @Test
    fun taskSuccessDialog_snapshot_dark() {
        runSuccessDialogSnapshot(
            darkTheme = true,
            name = "dialog_success_dark"
        )
    }

    @Test
    fun taskSuccessDialog_snapshot_long_message_light() {
        runSuccessDialogSnapshot(
            title = "Tarefa Salva com Sucesso!",
            message = "Sua tarefa foi armazenada localmente e sincronizada com a nuvem. " +
                "Você já pode visualizá-la na sua lista de tarefas pendentes na tela principal.",
            darkTheme = false,
            name = "dialog_success_long_message_light"
        )
    }

    private fun runSuccessDialogSnapshot(
        title: String = "Sucesso!",
        message: String = "A operação foi concluída.",
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskSuccessFancyDialog(
                    title = title,
                    message = message,
                    onConfirmClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeRule.waitForIdle()

        val dialogNode = composeRule.onNode(isDialog())

        compareScreenshot(node = dialogNode, name = name)
    }
}
