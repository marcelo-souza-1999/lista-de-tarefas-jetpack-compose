package com.marcelo.souza.listadetarefas.presentation.viewmodel

import app.cash.turbine.test
import com.marcelo.souza.listadetarefas.utils.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.ui.home.UiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<TaskRepository>(relaxed = true)
    private lateinit var viewModel: TaskFormViewModel

    @Before
    fun setup() {
        viewModel = TaskFormViewModel(repository)
    }

    @Test
    fun `onTitleChange should update uiState`() {
        viewModel.onTitleChange("Nova Tarefa")
        assertEquals("Nova Tarefa", viewModel.uiState.value.title)
    }

    @Test
    fun `onDescriptionChange should update uiState`() {
        viewModel.onDescriptionChange("Descrição teste")
        assertEquals("Descrição teste", viewModel.uiState.value.description)
    }

    @Test
    fun `onPriorityChange should update uiState`() {
        viewModel.onPriorityChange(TaskPriority.HIGH)
        assertEquals(TaskPriority.HIGH, viewModel.uiState.value.selectedPriority)
    }

    @Test
    fun `loadTask should populate uiState for editing`() {
        val task = Task(
            id = "123",
            title = "Task Edit",
            description = "Desc Edit",
            priority = TaskPriority.MEDIUM,
            isCompleted = true
        )

        viewModel.loadTask(task)

        val state = viewModel.uiState.value
        assertTrue(state.isEditing)
        assertEquals("123", state.taskId)
        assertEquals("Task Edit", state.title)
        assertEquals(TaskPriority.MEDIUM, state.selectedPriority)
        assertTrue(state.isCompleted)
    }

    @Test
    fun `saveTask should not proceed if title is blank`() = runTest {
        viewModel.onTitleChange("   ")
        viewModel.saveTask()
        coVerify(exactly = 0) { repository.saveTask(any()) }
    }

    @Test
    fun `saveTask should create new task successfully`() = runTest {
        coEvery { repository.saveTask(any()) } returns TaskResult.Success(true)

        viewModel.onTitleChange("Nova Task")

        viewModel.uiEvent.test {
            viewModel.saveTask()

            assertEquals(UiEvent.HideKeyboard, awaitItem())

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue("Deveria mostrar o diálogo de sucesso", state.showSuccessDialog)
            assertFalse("Não deveria estar em loading", state.isLoading)
        }
    }

    @Test
    fun `saveTask should handle repository error`() = runTest {
        val networkError = DataError.Network()

        coEvery { repository.saveTask(any()) } returns TaskResult.Error(networkError)

        viewModel = TaskFormViewModel(repository)

        viewModel.onTitleChange("Erro Teste")

        viewModel.uiEvent.test {
            viewModel.saveTask()

            awaitItem()

            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertEquals(networkError, state.error)
            assertFalse(state.isLoading)
        }

        coVerify { repository.saveTask(any()) }
    }

    @Test
    fun `saveTask should update existing task when in editing mode`() = runTest {
        coEvery { repository.updateTask(any()) } returns TaskResult.Success(true)

        val task = Task(id = "1", title = "Original", description = "", priority = TaskPriority.LOW)

        viewModel.loadTask(task)
        viewModel.onTitleChange("Título Editado")

        viewModel.uiEvent.test {
            viewModel.saveTask()

            awaitItem()

            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(state.showSuccessDialog)
            assertFalse(state.isLoading)
        }

        coVerify(exactly = 1) { repository.updateTask(any()) }
        coVerify(exactly = 0) { repository.saveTask(any()) }
    }

    @Test
    fun `onBackClicked should emit navigation event`() = runTest {
        viewModel.navigationEvent.test {
            viewModel.onBackClicked()
            assertEquals(NavigationEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onSuccessConfirmed should emit navigation event`() = runTest {
        viewModel.navigationEvent.test {
            viewModel.onSuccessConfirmed()
            assertEquals(NavigationEvent.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `dismissError should clear error in uiState`() = runTest {
        coEvery { repository.saveTask(any()) } returns TaskResult.Error(DataError.Unknown())
        viewModel.onTitleChange("T")
        viewModel.saveTask()
        advanceUntilIdle()

        viewModel.dismissError()
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `dismissSuccessDialog should hide dialog in uiState`() {
        viewModel.dismissSuccessDialog()
        assertFalse(viewModel.uiState.value.showSuccessDialog)
    }

    @Test
    fun `resetForm should return to initial state`() {
        viewModel.onTitleChange("Surja")
        viewModel.resetForm()

        val state = viewModel.uiState.value
        assertEquals("", state.title)
        assertFalse(state.isEditing)
    }
}