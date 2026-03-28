package com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskErrorFancyDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onRetryClick: () -> Unit = mockk(relaxed = true)
    private val onCancelClick: () -> Unit = mockk(relaxed = true)
    private val onDismissRequest: () -> Unit = mockk(relaxed = true)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val retryText = context.getString(R.string.title_button_positive_error_dialog_registration_task)
    private val cancelText = context.getString(R.string.title_button_negative_error_dialog_registration_task)

    @Test
    fun errorDialog_displaysTitleAndMessage() {
        val title = "Erro de Conexão"
        val message = "Falha ao sincronizar dados."

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskErrorFancyDialog(
                    title = title,
                    message = message,
                    onRetryClick = {},
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun errorDialog_whenRetryClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskErrorFancyDialog(
                    title = "Erro",
                    message = "Mensagem",
                    onRetryClick = onRetryClick,
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(retryText).performClick()

        verify { onRetryClick() }
    }

    @Test
    fun errorDialog_whenCancelClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskErrorFancyDialog(
                    title = "Erro",
                    message = "Mensagem",
                    onRetryClick = {},
                    onCancelClick = onCancelClick,
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(cancelText).performClick()

        verify { onCancelClick() }
    }

    @Test
    fun errorDialog_whenDismissedWithBack_callsOnDismissRequest() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskErrorFancyDialog(
                    title = "Erro",
                    message = "Mensagem",
                    isCancelable = true,
                    onRetryClick = {},
                    onCancelClick = {},
                    onDismissRequest = onDismissRequest
                )
            }
        }

        androidx.test.espresso.Espresso.pressBack()

        verify { onDismissRequest() }
    }
}