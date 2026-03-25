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
class TaskLogoutFancyDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onConfirmLogout: () -> Unit = mockk(relaxed = true)
    private val onCancelClick: () -> Unit = mockk(relaxed = true)
    private val onDismissRequest: () -> Unit = mockk(relaxed = true)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val confirmText = context.getString(R.string.title_button_confirm_logout)
    private val cancelText = context.getString(R.string.title_button_cancel_logout)

    @Test
    fun logoutDialog_displaysTitleAndMessage() {
        val title = "Logout"
        val message = "Deseja sair?"

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLogoutConfirmationFancyDialog(
                    title = title,
                    message = message,
                    onConfirmLogout = {},
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun logoutDialog_whenConfirmClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLogoutConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmLogout = onConfirmLogout,
                    onCancelClick = {},
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(confirmText).performClick()

        verify { onConfirmLogout() }
    }

    @Test
    fun logoutDialog_whenCancelClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLogoutConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmLogout = {},
                    onCancelClick = onCancelClick,
                    onDismissRequest = {}
                )
            }
        }

        composeTestRule.onNodeWithText(cancelText).performClick()

        verify { onCancelClick() }
    }

    @Test
    fun logoutDialog_whenDismissedWithBack_callsOnDismissRequest() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLogoutConfirmationFancyDialog(
                    title = "Title",
                    message = "Message",
                    onConfirmLogout = {},
                    onCancelClick = {},
                    onDismissRequest = onDismissRequest
                )
            }
        }

        Espresso.pressBack()

        verify { onDismissRequest() }
    }
}