package com.marcelo.souza.listadetarefas.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskLazyColumnTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onCheckedChange: (Task, Boolean) -> Unit = mockk(relaxed = true)
    private val onEditTask: (Task) -> Unit = mockk(relaxed = true)
    private val onDeleteTask: (Task) -> Unit = mockk(relaxed = true)

    private val fakeTasks = listOf(
        Task("1", "Task 1", "Desc 1", TaskPriority.HIGH, false),
        Task("2", "Task 2", "Desc 2", TaskPriority.LOW, true)
    )

    @Test
    fun taskLazyColumn_displaysAllTasks() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLazyColumn(
                    tasks = fakeTasks,
                    onTaskCheckedChange = onCheckedChange,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask
                )
            }
        }

        fakeTasks.forEach { task ->
            composeTestRule.onNodeWithText(task.title).assertIsDisplayed()
        }
    }

    @Test
    fun taskLazyColumn_whenSwipedRight_callsOnDeleteTask() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLazyColumn(
                    tasks = listOf(fakeTasks[0]),
                    onTaskCheckedChange = onCheckedChange,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask
                )
            }
        }

        composeTestRule
            .onNodeWithText(fakeTasks[0].title)
            .performTouchInput {
                swipeRight()
            }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            try {
                verify { onDeleteTask(fakeTasks[0]) }
                true
            } catch (_: Throwable) {
                false
            }
        }
    }

    @Test
    fun taskLazyColumn_whenSwipedLeft_callsOnEditTask() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskLazyColumn(
                    tasks = listOf(fakeTasks[0]),
                    onTaskCheckedChange = onCheckedChange,
                    onEditTask = onEditTask,
                    onDeleteTask = onDeleteTask
                )
            }
        }

        composeTestRule
            .onNodeWithText(fakeTasks[0].title)
            .performTouchInput {
                swipeLeft()
            }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            try {
                verify { onEditTask(fakeTasks[0]) }
                true
            } catch (_: Throwable) {
                false
            }
        }
    }
}