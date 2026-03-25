package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private val onCheckedChange: (Boolean) -> Unit = mockk(relaxed = true)
    private val onEditClick: () -> Unit = mockk(relaxed = true)
    private val onDeleteClick: () -> Unit = mockk(relaxed = true)

    private fun setContent(task: Task) {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskCard(
                    task = task,
                    onCheckedChange = onCheckedChange,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }

    @Test
    fun taskCard_whenPending_displaysCorrectLabelsAndColors() {
        val task = Task("1", "Tarefa Pendente", "Desc", TaskPriority.HIGH, isCompleted = false)
        setContent(task)

        composeTestRule.onNodeWithText("Tarefa Pendente").assertIsDisplayed()
        composeTestRule.onNodeWithText("Desc").assertIsDisplayed()

        val highLabel = context.getString(R.string.task_priority_high)
        composeTestRule.onNodeWithText(highLabel).assertIsDisplayed()

        val pendingText = context.getString(R.string.task_pending)
        composeTestRule.onNodeWithText(pendingText).assertIsDisplayed()
    }

    @Test
    fun taskCard_whenCompleted_displaysCompletedStatus() {
        val task = Task("1", "Tarefa Feita", "Desc", TaskPriority.LOW, isCompleted = true)
        setContent(task)

        val completedText = context.getString(R.string.task_completed)
        composeTestRule.onNodeWithText(completedText).assertIsDisplayed()

        val lowLabel = context.getString(R.string.task_priority_low)
        composeTestRule.onNodeWithText(lowLabel).assertIsDisplayed()
    }

    @Test
    fun taskCard_whenMediumPriority_coversPriorityBranch() {
        val task = Task("1", "Task", "Desc", TaskPriority.MEDIUM, isCompleted = false)
        setContent(task)

        val mediumLabel = context.getString(R.string.task_priority_medium)
        composeTestRule.onNodeWithText(mediumLabel).assertIsDisplayed()
    }

    @Test
    fun taskCard_interactions_callCorrectCallbacks() {
        val task = Task("1", "Teste Cliques", "Desc", TaskPriority.MEDIUM, isCompleted = false)
        setContent(task)

        composeTestRule.onNodeWithTag("check_box_card").performClick()
        verify { onCheckedChange(true) }

        composeTestRule.onNodeWithTag("edit_task_btn").performClick()
        verify { onEditClick() }

        composeTestRule.onNodeWithTag("delete_task_btn").performClick()
        verify { onDeleteClick() }
    }
}