package com.marcelo.souza.listadetarefas.data.mapper

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData

fun TaskDto.toDomain(): TaskViewData {
    return TaskViewData(
        id = id,
        title = title,
        description = description,
        priority = priority,
        isCompleted = isCompleted
    )
}

fun TaskViewData.toDto(): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        priority = priority,
        isCompleted = isCompleted
    )
}
