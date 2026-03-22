package com.marcelo.souza.listadetarefas.presentation.navigation.model

import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import kotlinx.serialization.Serializable

@Serializable
data class TaskNavigationModel(
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val isCompleted: Boolean
)

fun Task.toNavigationModel() = TaskNavigationModel(id, title, description, priority.name, isCompleted)

fun TaskNavigationModel.toDomainModel() = Task(id, title, description, TaskPriority.valueOf(priority), isCompleted)