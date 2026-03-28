package com.marcelo.souza.listadetarefas.domain.repository

import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun saveTask(task: Task): TaskResult<Boolean>

    fun getTasks(): Flow<TaskResult<List<Task>>>

    suspend fun updateTaskCompletion(
        taskId: String,
        isCompleted: Boolean
    ): TaskResult<Boolean>

    suspend fun updateTask(task: Task): TaskResult<Boolean>

    suspend fun deleteTask(taskId: String): TaskResult<Boolean>
}
