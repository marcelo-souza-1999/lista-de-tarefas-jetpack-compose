package com.marcelo.souza.listadetarefas.presentation.ui.task

import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority

data class TaskFormUiState(
    val title: String = "",
    val description: String = "",
    val selectedPriority: TaskPriority = TaskPriority.LOW,
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val showSuccessDialog: Boolean = false,
    val isEditing: Boolean = false,
    val isCompleted: Boolean = false,
    val taskId: String = ""
)