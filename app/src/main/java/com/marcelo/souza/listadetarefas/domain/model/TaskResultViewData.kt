package com.marcelo.souza.listadetarefas.domain.model

import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_HIGH
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_LOW
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_MEDIUM

sealed class TaskResultViewData<out T> {
    data class Success<out T>(val data: T) : TaskResultViewData<T>()
    data class Error(val error: DataError) : TaskResultViewData<Nothing>()
}

sealed class DataError : Throwable() {
    class Network : DataError()
    class Permission : DataError()
    class Unknown : DataError()
}

data class RegistrationUiState(
    val title: String = "",
    val description: String = "",
    val selectedPriority: TaskPriorityEnum = TaskPriorityEnum.LOW,
    val isLoading: Boolean = false,
    val error: DataError? = null,
    val showSuccessDialog: Boolean = false,
    val isEditing: Boolean = false,
    val isCompleted: Boolean = false,
    val taskId: String = ""
)

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
    data class Success(val tasks: List<TaskViewData>) : HomeUiState()
    data class Error(val error: DataError) : HomeUiState()
}

fun String.nextPriority(): String {
    return when (this) {
        PRIORITY_HIGH -> PRIORITY_MEDIUM
        PRIORITY_MEDIUM -> PRIORITY_LOW
        else -> PRIORITY_HIGH
    }
}