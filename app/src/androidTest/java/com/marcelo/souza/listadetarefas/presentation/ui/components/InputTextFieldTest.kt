package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onValueChange: (String) -> Unit = mockk(relaxed = true)

    @Test
    fun inputTextField_whenTyping_callsOnValueChange() {
        val label = "E-mail"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                InputTextField(
                    text = "",
                    onValueText = onValueChange,
                    label = label,
                    keyboardOptions = KeyboardOptions.Default
                )
            }
        }

        composeTestRule.onNodeWithText(label).performTextInput("marcelo@teste.com")

        verify { onValueChange("marcelo@teste.com") }
    }

    @Test
    fun inputTextField_whenIsErrorTrue_displaysErrorMessage() {
        val errorMessage = "Campo obrigatório"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                InputTextField(
                    text = "",
                    onValueText = {},
                    label = "Nome",
                    isError = true,
                    errorMessage = errorMessage,
                    keyboardOptions = KeyboardOptions.Default
                )
            }
        }

        composeTestRule.onNodeWithTag("error_message_input_text").assertIsDisplayed()

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun inputTextField_whenIsErrorAndNoTrailingIcon_showsDefaultErrorIcon() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                InputTextField(
                    text = "",
                    onValueText = {},
                    label = "Erro",
                    isError = true,
                    keyboardOptions = KeyboardOptions.Default
                )
            }
        }
    }
}