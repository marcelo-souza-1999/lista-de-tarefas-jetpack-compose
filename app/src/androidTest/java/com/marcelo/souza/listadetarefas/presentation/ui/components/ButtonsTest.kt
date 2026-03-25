package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onButtonClick: () -> Unit = mockk(relaxed = true)

    @Test
    fun primaryButton_whenEnabled_displaysTextAndCallsOnClick() {
        val buttonText = "Entrar"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                PrimaryButton(text = buttonText, onClick = onButtonClick)
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        verify { onButtonClick() }
    }

    @Test
    fun primaryButton_whenDisabled_doesNotCallOnClick() {
        val buttonText = "Desabilitado"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                PrimaryButton(text = buttonText, onClick = onButtonClick, enabled = false)
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assertIsNotEnabled()
            .performClick()

        verify(exactly = 0) { onButtonClick() }
    }

    @Test
    fun primaryButton_whenLoading_displaysProgressIndicatorAndIsDisabled() {
        val buttonText = "Loading"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                PrimaryButton(text = buttonText, onClick = onButtonClick, isLoading = true)
            }
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(androidx.compose.ui.semantics.ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(buttonText).assertDoesNotExist()
    }

    @Test
    fun secondaryButton_displaysTextAndCallsOnClick() {
        val buttonText = "Cadastre-se"
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                SecondaryButton(text = buttonText, onClick = onButtonClick)
            }
        }

        composeTestRule.onNodeWithText(buttonText)
            .assertIsDisplayed()
            .performClick()

        verify { onButtonClick() }
    }
}