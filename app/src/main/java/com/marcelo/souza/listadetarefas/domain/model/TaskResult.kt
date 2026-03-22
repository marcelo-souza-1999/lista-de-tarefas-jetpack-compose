package com.marcelo.souza.listadetarefas.domain.model

sealed class TaskResult<out T> {
    data class Success<out T>(val data: T) : TaskResult<T>()
    data class Error(val error: DataError) : TaskResult<Nothing>()
}

sealed class DataError : Throwable() {
    class Network : DataError()
    class Permission : DataError()
    class Unknown : DataError()
}