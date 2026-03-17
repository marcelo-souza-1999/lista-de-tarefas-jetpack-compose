package com.marcelo.souza.listadetarefas.data.model

data class TaskDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val priority: String = "",
    val isCompleted: Boolean = false
)
