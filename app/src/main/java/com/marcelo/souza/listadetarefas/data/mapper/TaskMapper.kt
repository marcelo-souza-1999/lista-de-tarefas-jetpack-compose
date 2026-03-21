package com.marcelo.souza.listadetarefas.data.mapper

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority

fun TaskDto.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        priority = try {
            TaskPriority.valueOf(priority.uppercase())
        } catch (_: Exception) {
            TaskPriority.LOW
        },
        isCompleted = completed
    )
}

fun Task.toDto(userId: String): TaskDto {
    return TaskDto(
        id = id,
        userId = userId,
        title = title,
        description = description,
        priority = priority.name,
        completed = isCompleted
    )
}