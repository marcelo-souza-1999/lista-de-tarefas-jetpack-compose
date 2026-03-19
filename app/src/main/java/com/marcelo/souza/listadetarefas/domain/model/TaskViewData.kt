package com.marcelo.souza.listadetarefas.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskViewData(
    val id: String = "",
    val title: String,
    val description: String,
    val priority: String,
    val isCompleted: Boolean = false
)