package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskLazyColumn
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskLazyColumnSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeTasks = listOf(
        Task("1", "Tarefa Alta Prioridade", "Desc 1", TaskPriority.HIGH, false),
        Task("2", "Tarefa Média Concluída", "Desc 2", TaskPriority.MEDIUM, true),
        Task("3", "Tarefa Baixa Pendente", "Desc 3", TaskPriority.LOW, false),
        Task("4", "Outra Tarefa Alta", "Desc 4", TaskPriority.HIGH, false)
    )

    @Test
    fun taskLazyColumn_snapshot_light_multi_items() {
        runLazyColumnSnapshot(fakeTasks, darkTheme = false, name = "lazy_column_multi_light")
    }

    @Test
    fun taskLazyColumn_snapshot_dark_multi_items() {
        runLazyColumnSnapshot(fakeTasks, darkTheme = true, name = "lazy_column_multi_dark")
    }

    private fun runLazyColumnSnapshot(tasks: List<Task>, darkTheme: Boolean, name: String) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskLazyColumn(
                    tasks = tasks,
                    onTaskCheckedChange = { _, _ -> },
                    onEditTask = {},
                    onDeleteTask = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}