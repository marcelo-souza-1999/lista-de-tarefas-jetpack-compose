package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.model.RegistrationUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskPriorityEnum
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.model.UiEvent
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegistrationTaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onTitleChange(newValue: String) {
        _uiState.value = _uiState.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.value = _uiState.value.copy(description = newValue)
    }

    fun onPriorityChange(newValue: TaskPriorityEnum) {
        _uiState.value = _uiState.value.copy(selectedPriority = newValue)
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateBack)
        }
    }

    fun loadTask(task: TaskViewData) {
        _uiState.value = _uiState.value.copy(
            title = task.title,
            description = task.description,
            selectedPriority = TaskPriorityEnum.valueOf(task.priority),
            isEditing = true,
            taskId = task.id,
            isCompleted = task.isCompleted
        )
    }

    fun saveTask() {
        val currentState = _uiState.value

        if (currentState.isLoading || currentState.title.trim().isBlank()) return

        viewModelScope.launch {
            _uiEvent.send(UiEvent.HideKeyboard)

            _uiState.value = currentState.copy(
                isLoading = true,
                error = null
            )

            val result = if (currentState.isEditing) {
                repository.updateTask(
                    TaskViewData(
                        id = currentState.taskId,
                        title = currentState.title,
                        description = currentState.description,
                        priority = currentState.selectedPriority.name,
                        isCompleted = currentState.isCompleted
                    )
                )
            } else {
                repository.saveTask(
                    TaskViewData(
                        title = currentState.title.trim(),
                        description = currentState.description.trim(),
                        priority = currentState.selectedPriority.name,
                        isCompleted = currentState.isCompleted
                    )
                )
            }

            _uiState.value = when (result) {
                is TaskResultViewData.Success -> {
                    currentState.copy(
                        isLoading = false,
                        showSuccessDialog = true
                    )
                }

                is TaskResultViewData.Error -> {
                    currentState.copy(
                        isLoading = false,
                        error = result.error
                    )
                }
            }
        }
    }

    fun onSuccessConfirmed() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateBack)
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
    }

    fun resetForm() {
        _uiState.value = RegistrationUiState()
    }
}