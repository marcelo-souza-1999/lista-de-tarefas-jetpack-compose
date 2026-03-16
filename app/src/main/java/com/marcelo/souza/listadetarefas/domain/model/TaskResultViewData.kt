package com.marcelo.souza.listadetarefas.domain.model

sealed class TaskResultViewData<out T> {
    data class Success<out T>(val data: T) : TaskResultViewData<T>()
    data class Error(val error: DataError) : TaskResultViewData<Nothing>()
}

sealed class DataError : Throwable() {
    class Network : DataError()
    class Permission : DataError()
    class Unknown : DataError()
}

sealed class RegistrationUiState {
    object Idle : RegistrationUiState()
    object Loading : RegistrationUiState()
    object Success : RegistrationUiState()
    data class Error(val error: DataError) : RegistrationUiState()
}

sealed class UiEvent {
    object HideKeyboard : UiEvent()
}