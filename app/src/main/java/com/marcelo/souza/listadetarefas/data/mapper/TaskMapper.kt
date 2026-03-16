package com.marcelo.souza.listadetarefas.data.mapper

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData

// Converte do modelo de banco para o modelo de domínio (UI)
fun TaskDto.toDomain(): TaskViewData {
    return TaskViewData(
        title = this.title,
        description = this.description,
        priority = this.priority
    )
}

// Converte do modelo de domínio para o modelo de banco (para salvar)
fun TaskViewData.toDto(): TaskDto {
    return TaskDto(
        title = this.title,
        description = this.description,
        priority = this.priority
    )
}