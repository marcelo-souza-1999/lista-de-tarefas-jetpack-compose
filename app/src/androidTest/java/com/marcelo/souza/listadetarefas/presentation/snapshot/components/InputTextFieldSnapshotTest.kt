package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.InputTextField
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputTextFieldSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun inputTextField_snapshot_default_light() {
        runInputSnapshot(
            text = "marcelo@souza.com",
            label = "E-mail",
            darkTheme = false,
            name = "input_default_light"
        )
    }

    @Test
    fun inputTextField_snapshot_error_dark() {
        runInputSnapshot(
            text = "email-invalido",
            label = "E-mail",
            isError = true,
            errorMessage = "Formato de e-mail inválido",
            darkTheme = true,
            name = "input_error_dark"
        )
    }

    @Test
    fun inputTextField_snapshot_with_icons_light() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                InputTextField(
                    text = "minhasenha123",
                    onValueText = {},
                    label = "Senha",
                    keyboardOptions = KeyboardOptions.Default,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.Visibility, contentDescription = null) }
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "input_with_icons_light")
    }

    private fun runInputSnapshot(
        text: String,
        label: String,
        isError: Boolean = false,
        errorMessage: String? = null,
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                InputTextField(
                    text = text,
                    onValueText = {},
                    label = label,
                    isError = isError,
                    errorMessage = errorMessage,
                    keyboardOptions = KeyboardOptions.Default
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}