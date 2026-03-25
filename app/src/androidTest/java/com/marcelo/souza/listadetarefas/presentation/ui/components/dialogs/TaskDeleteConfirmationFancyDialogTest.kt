package com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class TaskDeleteConfirmationFancyDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onConfirmDelete: () -> Unit = mockk(relaxed = true)
    private val onCancelClick: () -> Unit = mockk(relaxed = true)
    private val onDismissRequest: () -> Unit = mockk(relaxed = true)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val confirmText = context.getString(R.string.title_button_delete_confirm)
    private val cancelText = context.getString(R.string.title_button_cancel_delete_confirm)

    @Test
    fun deleteDialog_displaysTitleAndMessage() {
        val title = "Excluir?"
        val message = "Certeza absoluta?"

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskDeleteConfirmationFancyDialog(
                    title = title,
                    message = message,
                    onConfirmDelete = {},
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun deleteDialog_whenConfirmClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskDeleteConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmDelete = onConfirmDelete,
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(confirmText).performClick()

        verify { onConfirmDelete() }
    }

    @Test
    fun deleteDialog_whenCancelClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskDeleteConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmDelete = {},
                    onCancelClick = onCancelClick,
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(cancelText).performClick()

        verify { onCancelClick() }
    }

    @Test
    fun deleteDialog_whenDismissedOutside_callsOnDismissRequest() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskDeleteConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmDelete = {},
                    onCancelClick = {},
                    onDismissRequest = onDismissRequest
                )
            }
        }

        Espresso.pressBack()

        verify { onDismissRequest() }
    }

    @Test
    fun deleteDialog_whenDismissedWithBack_callsOnDismissRequest() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskDeleteConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmDelete = {},
                    onCancelClick = {},
                    onDismissRequest = onDismissRequest
                )
            }
        }

        Espresso.pressBack()

        verify { onDismissRequest() }
    }
}