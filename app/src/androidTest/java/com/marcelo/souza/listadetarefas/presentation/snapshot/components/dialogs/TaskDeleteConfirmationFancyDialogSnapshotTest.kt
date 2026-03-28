package com.marcelo.souza.listadetarefas.presentation.snapshot.components.dialogs

import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskDeleteConfirmationFancyDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDeleteConfirmationFancyDialogSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deleteDialog_snapshot_light() {
        runDeleteDialogSnapshot(
            darkTheme = false,
            name = "dialog_delete_light"
        )
    }

    @Test
    fun deleteDialog_snapshot_dark() {
        runDeleteDialogSnapshot(
            darkTheme = true,
            name = "dialog_delete_dark"
        )
    }

    private fun runDeleteDialogSnapshot(
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskDeleteConfirmationFancyDialog(
                    title = "Excluir Tarefa",
                    message = "Tem certeza que deseja excluir a tarefa 'Estudar Compose'? Esta ação não pode ser desfeita.",
                    onConfirmDelete = {},
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