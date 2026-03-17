package com.marcelo.souza.listadetarefas.domain.repository

import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData

interface TaskRepository {
    suspend fun saveTask(task: TaskViewData): TaskResultViewData<Boolean>
    suspend fun getTasks(): TaskResultViewData<List<TaskViewData>>
    suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResultViewData<Boolean>
}
