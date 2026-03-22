package com.marcelo.souza.listadetarefas.presentation.viewmodel

import com.marcelo.souza.listadetarefas.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.ui.home.UiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TaskFormViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<TaskRepository>()

    @Test
    fun `saveTask should emit HideKeyboard and show success on create`() = runTest {
        coEvery { repository.saveTask(any()) } returns TaskResult.Success(true)
        val viewModel = TaskFormViewModel(repository)
        viewModel.onTitleChange("Task")
        viewModel.onDescriptionChange("Description")

        viewModel.saveTask()

        assertEquals(UiEvent.HideKeyboard, viewModel.uiEvent.first())
        assertTrue(viewModel.uiState.value.showSuccessDialog)
    }

    @Test
    fun `saveTask should call update when editing`() = runTest {
        coEvery { repository.updateTask(any()) } returns TaskResult.Success(true)
        val viewModel = TaskFormViewModel(repository)
        viewModel.loadTask(Task(id = "1", title = "old", description = "d", priority = TaskPriority.HIGH))
        viewModel.onTitleChange("new")

        viewModel.saveTask()

        coVerify { repository.updateTask(any()) }
    }

    @Test
    fun `onBackClicked should emit navigateBack`() = runTest {
        val viewModel = TaskFormViewModel(repository)

        viewModel.onBackClicked()

        assertEquals(NavigationEvent.NavigateBack, viewModel.navigationEvent.first())
    }
}
