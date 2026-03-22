package com.marcelo.souza.listadetarefas.domain.model

data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val isCompleted: Boolean = false
)