package com.marcelo.souza.listadetarefas.data.datasource

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData

interface TaskDataSource {
    suspend fun saveTask(task: TaskDto): TaskResultViewData<Boolean>
    suspend fun getTasks(): List<TaskDto>
}