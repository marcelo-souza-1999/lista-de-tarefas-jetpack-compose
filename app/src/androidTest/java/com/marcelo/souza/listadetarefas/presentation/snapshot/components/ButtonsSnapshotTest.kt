package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.PrimaryButton
import com.marcelo.souza.listadetarefas.presentation.ui.components.SecondaryButton
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonsSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun primaryButton_snapshot_enabled_light() {
        runButtonSnapshot(darkTheme = false, name = "primary_button_enabled_light") {
            PrimaryButton(text = "Botão Primário", onClick = {})
        }
    }

    @Test
    fun primaryButton_snapshot_disabled_dark() {
        runButtonSnapshot(darkTheme = true, name = "primary_button_disabled_dark") {
            PrimaryButton(text = "Botão Desabilitado", onClick = {}, enabled = false)
        }
    }

    @Test
    fun primaryButton_snapshot_loading_light() {
        runButtonSnapshot(darkTheme = false, name = "primary_button_loading_light") {
            PrimaryButton(text = "Entrar", onClick = {}, isLoading = true)
        }
    }

    @Test
    fun secondaryButton_snapshot_light() {
        runButtonSnapshot(darkTheme = false, name = "secondary_button_light") {
            SecondaryButton(text = "Botão Secundário", onClick = {})
        }
    }

    @Test
    fun secondaryButton_snapshot_dark() {
        runButtonSnapshot(darkTheme = true, name = "secondary_button_dark") {
            SecondaryButton(text = "Botão Secundário Dark", onClick = {})
        }
    }

    private fun runButtonSnapshot(
        darkTheme: Boolean,
        name: String,
        content: @Composable () -> Unit
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                content()
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}