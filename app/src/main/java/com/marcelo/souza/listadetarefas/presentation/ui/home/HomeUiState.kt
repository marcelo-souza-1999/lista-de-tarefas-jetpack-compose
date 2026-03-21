package com.marcelo.souza.listadetarefas.presentation.ui.home

import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task

sealed class UiEvent {
    object HideKeyboard : UiEvent()
}

enum class TaskFilter {
    ALL,
    PENDING,
    COMPLETED
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val tasks: List<Task>) : HomeUiState()
    data class Error(val error: DataError) : HomeUiState()
}