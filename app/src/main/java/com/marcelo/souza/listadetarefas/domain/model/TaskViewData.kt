package com.marcelo.souza.listadetarefas.domain.model

data class TaskViewData(
    val id: String = "",
    val title: String,
    val description: String,
    val priority: String,
    val isCompleted: Boolean = false
)

enum class TaskFilter {
    ALL,
    PENDING,
    COMPLETED
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val tasks: List<TaskViewData>) : HomeUiState()
    data class Error(val error: DataError) : HomeUiState()
}
