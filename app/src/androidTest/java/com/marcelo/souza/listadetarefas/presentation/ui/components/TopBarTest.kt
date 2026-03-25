package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onLogoutClick: () -> Unit = mockk(relaxed = true)
    private val onBackClick: () -> Unit = mockk(relaxed = true)

    @Test
    fun topBar_displaysUserNameAndGreeting() {
        val userName = "Marcelo Souza"

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TopBar(
                    userName = userName,
                    onLogoutClick = onLogoutClick
                )
            }
        }

        composeTestRule.onNodeWithText(userName).assertIsDisplayed()

        composeTestRule.onNodeWithTag("logout_btn").assertIsDisplayed()
    }

    @Test
    fun topBar_whenLogoutClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TopBar(
                    userName = "User",
                    onLogoutClick = onLogoutClick
                )
            }
        }

        composeTestRule.onNodeWithTag("logout_btn").performClick()

        verify { onLogoutClick() }
    }

    @Test
    fun secondaryTopBar_displaysTitle() {
        val title = "Nova Tarefa"

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                SecondaryTopBar(
                    title = title,
                    onBackClick = onBackClick
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()

        composeTestRule.onNodeWithTag("back_btn").assertIsDisplayed()
    }

    @Test
    fun secondaryTopBar_whenBackClicked_callsCallback() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                SecondaryTopBar(
                    title = "Título",
                    onBackClick = onBackClick
                )
            }
        }

        composeTestRule.onNodeWithTag("back_btn").performClick()

        verify { onBackClick() }
    }
}