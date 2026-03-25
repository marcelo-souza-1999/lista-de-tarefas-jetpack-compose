package com.marcelo.souza.listadetarefas.presentation.viewmodel

import app.cash.turbine.test
import com.marcelo.souza.listadetarefas.utils.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.model.TaskFormKey
import com.marcelo.souza.listadetarefas.presentation.ui.home.HomeUiState
import com.marcelo.souza.listadetarefas.presentation.ui.home.TaskFilter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val taskRepository = mockk<TaskRepository>(relaxed = true)
    private val authRepository = mockk<AuthenticateRepository>(relaxed = true)

    private val fakeTasks = listOf(
        Task(
            id = "1",
            title = "Task 1",
            description = "description 1",
            isCompleted = false,
            priority = TaskPriority.LOW
        ),
        Task(
            id = "2",
            title = "Task 2",
            description = "description 2",
            isCompleted = true,
            priority = TaskPriority.HIGH
        )
    )

    @Before
    fun setup() {
        coEvery { authRepository.fetchUserName() } returns "Marcelo Souza"
        every { taskRepository.getTasks() } returns flowOf(TaskResult.Success(fakeTasks))
    }

    @Test
    fun `uiState should emit Success with filtered tasks when filter changes`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.uiState.test {
            val firstState = awaitItem()
            assertTrue(firstState is HomeUiState.Success)
            assertEquals(2, (firstState as HomeUiState.Success).tasks.size)

            viewModel.onFilterChange(TaskFilter.PENDING)

            val secondState = awaitItem()
            val filteredTasks = (secondState as HomeUiState.Success).tasks
            assertEquals(1, filteredTasks.size)
            assertFalse(filteredTasks.first().isCompleted)

            viewModel.onFilterChange(TaskFilter.COMPLETED)

            val thirdState = awaitItem()
            assertEquals(1, (thirdState as HomeUiState.Success).tasks.size)
            assertTrue(thirdState.tasks.first().isCompleted)
        }
    }

    @Test
    fun `logout should clear dialog and send navigation event to Login`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.navigationEvent.test {
            viewModel.onLogoutClick()
            assertTrue(viewModel.showLogoutDialog.value)

            viewModel.logout()

            assertFalse(viewModel.showLogoutDialog.value)

            assertEquals(NavigationEvent.NavigateAndClear(LoginKey), awaitItem())

            coVerify { authRepository.logout() }
        }
    }

    @Test
    fun `onTaskCheckedChange should update repository and show error dialog on failure`() =
        runTest {
            val error = DataError.Network()
            coEvery { taskRepository.updateTaskCompletion(any(), any()) } returns TaskResult.Error(
                error
            )

            val viewModel = HomeViewModel(taskRepository, authRepository)
            val task = fakeTasks.first()

            viewModel.onTaskCheckedChange(task, true)

            assertEquals(error, viewModel.dialogError.value)

            viewModel.dismissDialog(HomeViewModel.HomeDialogType.ERROR)
            assertNull(viewModel.dialogError.value)
        }

    @Test
    fun `fetchUserDisplayName should update userName on init`() = runTest {
        coEvery { authRepository.fetchUserName() } returns "Madara"

        val viewModel = HomeViewModel(taskRepository, authRepository)

        assertEquals("Madara", viewModel.userName.value)
    }

    @Test
    fun `uiState should emit Error when repository returns failure`() = runTest {
        val networkError = DataError.Network()
        every { taskRepository.getTasks() } returns flowOf(TaskResult.Error(networkError))

        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Error)
            assertEquals(networkError, (state as HomeUiState.Error).error)
        }
    }

    @Test
    fun `confirmDeleteTask should NOT call repository if pending task is null`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.confirmDeleteTask()

        coVerify(exactly = 0) { taskRepository.deleteTask(any()) }
    }

    @Test
    fun `onEditTask should send correct navigation event with task data`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)
        val task = fakeTasks.first()

        viewModel.navigationEvent.test {
            viewModel.onEditTask(task)

            val event = awaitItem() as NavigationEvent.Navigate
            val key = event.route as TaskFormKey

            assertEquals(task.id, key.task?.id)
            assertEquals(task.title, key.task?.title)
        }
    }

    @Test
    fun `dismissDialog should clear the correct state based on DialogType`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)
        val task = fakeTasks.first()

        viewModel.requestDeleteTask(task)
        assertEquals(task, viewModel.taskPendingDelete.value)
        viewModel.dismissDialog(HomeViewModel.HomeDialogType.DELETE)
        assertNull(viewModel.taskPendingDelete.value)

        viewModel.onTaskCheckedChange(task, true) // Se falhar, gera erro
        viewModel.dismissDialog(HomeViewModel.HomeDialogType.ERROR)
        assertNull(viewModel.dialogError.value)

        viewModel.onLogoutClick()
        assertTrue(viewModel.showLogoutDialog.value)
        viewModel.dismissDialog(HomeViewModel.HomeDialogType.LOGOUT)
        assertFalse(viewModel.showLogoutDialog.value)
    }

    @Test
    fun `onTaskCheckedChange should call repository and NOT update error on success`() = runTest {
        coEvery { taskRepository.updateTaskCompletion(any(), any()) } returns TaskResult.Success(
            true
        )
        val viewModel = HomeViewModel(taskRepository, authRepository)
        val task = fakeTasks.first()

        viewModel.onTaskCheckedChange(task, true)

        coVerify { taskRepository.updateTaskCompletion(task.id, true) }
        assertNull(viewModel.dialogError.value)
    }

    @Test
    fun `onAddTaskClicked should send navigation event for new task`() = runTest {
        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.navigationEvent.test {
            viewModel.onAddTaskClicked()

            val event = awaitItem() as NavigationEvent.Navigate
            val key = event.route as TaskFormKey

            assertNull(key.task)
        }
    }

    @Test
    fun `confirmDeleteTask failure should set dialogError and clear pendingTask`() = runTest {
        val error = DataError.Network()
        coEvery { taskRepository.deleteTask(any()) } returns TaskResult.Error(error)

        val viewModel = HomeViewModel(taskRepository, authRepository)
        val task = fakeTasks.first()

        viewModel.requestDeleteTask(task)
        viewModel.confirmDeleteTask()

        assertEquals(error, viewModel.dialogError.value)
        assertNull(viewModel.taskPendingDelete.value)
        coVerify { taskRepository.deleteTask(task.id) }
    }
}