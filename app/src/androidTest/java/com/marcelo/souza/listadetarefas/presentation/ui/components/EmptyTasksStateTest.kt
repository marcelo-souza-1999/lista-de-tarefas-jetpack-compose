package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyTasksStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyTasksState_displaysCorrectTitleAndDescription() {
        val testTitle = "Título Vazio"
        val testDescription = "Descrição do estado vazio para teste."

        composeTestRule.setContent {
            ListaDeTarefasTheme {
                EmptyTasksState(
                    title = testTitle,
                    description = testDescription
                )
            }
        }

        composeTestRule.onNodeWithText(testTitle)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(testDescription)
            .assertIsDisplayed()
    }
}