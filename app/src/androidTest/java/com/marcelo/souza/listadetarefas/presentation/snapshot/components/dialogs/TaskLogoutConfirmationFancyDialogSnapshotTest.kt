package com.marcelo.souza.listadetarefas.presentation.snapshot.components.dialogs

import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskLogoutConfirmationFancyDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskLogoutConfirmationFancyDialogSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun logoutDialog_snapshot_light() {
        runLogoutDialogSnapshot(
            darkTheme = false,
            name = "dialog_logout_light"
        )
    }

    @Test
    fun logoutDialog_snapshot_dark() {
        runLogoutDialogSnapshot(
            darkTheme = true,
            name = "dialog_logout_dark"
        )
    }

    private fun runLogoutDialogSnapshot(
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskLogoutConfirmationFancyDialog(
                    title = "Sair do Aplicativo",
                    message = "Tem certeza que deseja sair? Você precisará fazer login novamente para acessar suas tarefas.",
                    onConfirmLogout = {},
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