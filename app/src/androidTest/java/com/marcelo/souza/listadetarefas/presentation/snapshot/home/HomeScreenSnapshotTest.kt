package com.marcelo.souza.listadetarefas.presentation.snapshot.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.home.HomeScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.home.TaskFilter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeTasks = listOf(
        Task(
            "1",
            "Estudar Jetpack Compose",
            "Finalizar os testes de snapshot",
            TaskPriority.HIGH,
            false
        ),
        Task("2", "Academia", "Treino de pernas", TaskPriority.MEDIUM, true),
        Task("3", "Comprar café", "O grão acabou!", TaskPriority.LOW, false)
    )

    @Test
    fun homeScreen_snapshot_light_with_tasks() {
        runHomeSnapshot(tasks = fakeTasks, isLoading = false, darkTheme = false, name = "home_light_tasks")
    }

    @Test
    fun homeScreen_snapshot_light_empty() {
        runHomeSnapshot(tasks = emptyList(), isLoading = false, darkTheme = false, name = "home_light_empty")
    }

    @Test
    fun homeScreen_snapshot_light_loading() {
        runHomeSnapshot(tasks = emptyList(), isLoading = true, darkTheme = false, name = "home_light_loading")
    }

    @Test
    fun homeScreen_snapshot_dark_with_tasks() {
        runHomeSnapshot(tasks = fakeTasks, isLoading = false, darkTheme = true, name = "home_dark_tasks")
    }

    @Test
    fun homeScreen_snapshot_dark_empty() {
        runHomeSnapshot(tasks = emptyList(), isLoading = false, darkTheme = true, name = "home_dark_empty")
    }

    @Test
    fun homeScreen_snapshot_filter_completed_selected() {
        runHomeSnapshot(
            tasks = fakeTasks.filter { it.isCompleted },
            isLoading = false,
            darkTheme = false,
            filter = TaskFilter.COMPLETED,
            name = "home_filter_completed"
        )
    }

    private fun runHomeSnapshot(
        tasks: List<Task>,
        isLoading: Boolean,
        darkTheme: Boolean,
        filter: TaskFilter = TaskFilter.ALL,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                HomeScreenContent(
                    tasks = tasks,
                    isLoading = isLoading,
                    selectedFilter = filter,
                    userName = "Marcelo Souza",
                    onFilterChange = {},
                    onAddTaskClick = {},
                    onTaskCheckedChange = { _, _ -> },
                    onDeleteTask = {},
                    onEditTask = {},
                    onLogoutClick = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}