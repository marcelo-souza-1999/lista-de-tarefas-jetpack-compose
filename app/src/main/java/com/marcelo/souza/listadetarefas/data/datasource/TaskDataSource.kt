package com.marcelo.souza.listadetarefas.data.datasource

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    suspend fun saveTask(task: TaskDto): TaskResult<Boolean>

    fun getTasks(): Flow<TaskResult<List<TaskDto>>>

    suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResult<Boolean>

    suspend fun updateTask(taskId: String, task: TaskDto): TaskResult<Boolean>

    suspend fun deleteTask(taskId: String): TaskResult<Boolean>
}
