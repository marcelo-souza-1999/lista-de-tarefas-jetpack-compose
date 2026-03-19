package com.marcelo.souza.listadetarefas.domain.repository

import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun saveTask(task: TaskViewData): TaskResultViewData<Boolean>
    fun getTasks(): Flow<TaskResultViewData<List<TaskViewData>>>
    suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResultViewData<Boolean>
    suspend fun updateTask(task: TaskViewData): TaskResultViewData<Boolean>
    suspend fun deleteTask(taskId: String): TaskResultViewData<Boolean>
}
