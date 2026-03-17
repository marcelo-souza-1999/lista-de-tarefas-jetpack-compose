package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.model.RegistrationUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskPriorityEnum
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.model.UiEvent
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
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

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var selectedPriority by mutableStateOf(TaskPriorityEnum.LOW)
        private set

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onTitleChange(newValue: String) {
        title = newValue
    }

    fun onDescriptionChange(newValue: String) {
        description = newValue
    }

    fun onPriorityChange(newValue: TaskPriorityEnum) {
        selectedPriority = newValue
    }

    fun saveTask() {
        if (_uiState.value is RegistrationUiState.Loading || title.trim().isBlank()) return

        viewModelScope.launch {
            _uiEvent.send(UiEvent.HideKeyboard)
            _uiState.value = RegistrationUiState.Loading

            val result = repository.saveTask(
                TaskViewData(
                    title = title.trim(),
                    description = description.trim(),
                    priority = selectedPriority.name,
                    isCompleted = false
                )
            )

            _uiState.value = when (result) {
                is TaskResultViewData.Success -> RegistrationUiState.Success
                is TaskResultViewData.Error -> RegistrationUiState.Error(result.error)
            }
        }
    }

    fun clearErrorState() {
        if (_uiState.value is RegistrationUiState.Error) {
            _uiState.value = RegistrationUiState.Idle
        }
    }
}
