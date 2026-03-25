package com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
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
class TaskSuccessFancyDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onConfirmClick: () -> Unit = mockk(relaxed = true)
    private val onDismissRequest: () -> Unit = mockk(relaxed = true)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val buttonText = context.getString(R.string.title_button_success_dialog_registration_task)

    @Test
    fun successDialog_displaysTitleAndMessage() {
        val title = "Parabéns"
        val message = "Tarefa concluída!"

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskSuccessFancyDialog(
                    title = title,
                    message = message,
                    onConfirmClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun successDialog_whenConfirmClicked_callsBothCallbacks() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskSuccessFancyDialog(
                    title = "Sucesso",
                    message = "Mensagem",
                    onConfirmClick = onConfirmClick,
                    onDismissRequest = onDismissRequest
                )
            }
        }

        composeTestRule.onNodeWithText(buttonText).performClick()

        verify(exactly = 1) { onConfirmClick() }
        verify(exactly = 1) { onDismissRequest() }
    }

    @Test
    fun successDialog_whenDismissedWithBack_callsOnDismissRequest() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskSuccessFancyDialog(
                    title = "Sucesso",
                    message = "Mensagem",
                    isCancelable = true,
                    onConfirmClick = {},
                    onDismissRequest = onDismissRequest
                )
            }
        }

        Espresso.pressBack()

        verify(exactly = 1) { onDismissRequest() }
    }
}