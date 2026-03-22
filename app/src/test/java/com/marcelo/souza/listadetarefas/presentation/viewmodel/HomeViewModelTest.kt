package com.marcelo.souza.listadetarefas.presentation.viewmodel

import com.marcelo.souza.listadetarefas.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val taskRepository = mockk<TaskRepository>()
    private val authRepository = mockk<AuthenticateRepository>()

    @Test
    fun `onAddTaskClicked should send navigation event`() = runTest {
        coEvery { authRepository.fetchUserName() } returns "Marcelo"
        every { taskRepository.getTasks() } returns flowOf(TaskResult.Success(emptyList()))
        val viewModel = HomeViewModel(taskRepository, authRepository)

        viewModel.onAddTaskClicked()

        assertTrue(viewModel.navigationEvent.first() is NavigationEvent.Navigate)
    }

    @Test
    fun `confirmDeleteTask should call repository delete`() = runTest {
        coEvery { authRepository.fetchUserName() } returns "Marcelo"
        every { taskRepository.getTasks() } returns flowOf(TaskResult.Success(emptyList()))
        coEvery { taskRepository.deleteTask(any()) } returns TaskResult.Success(true)
        val viewModel = HomeViewModel(taskRepository, authRepository)
        val task = Task(id = "1", title = "t", description = "d", priority = TaskPriority.LOW)

        viewModel.requestDeleteTask(task)
        viewModel.confirmDeleteTask()

        coVerify { taskRepository.deleteTask("1") }
        assertEquals(null, viewModel.taskPendingDelete.value)
    }
}
