package com.marcelo.souza.listadetarefas.data.repository

import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
import com.marcelo.souza.listadetarefas.data.mapper.toDomain
import com.marcelo.souza.listadetarefas.data.mapper.toDto
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {

    override suspend fun saveTask(task: TaskViewData): TaskResultViewData<Boolean> {
        return taskDataSource.saveTask(task.toDto())
    }

    override fun getTasks(): Flow<TaskResultViewData<List<TaskViewData>>> {
        return taskDataSource.getTasks().map { result ->
            when (result) {
                is TaskResultViewData.Success -> TaskResultViewData.Success(result.data.map { it.toDomain() })
                is TaskResultViewData.Error -> TaskResultViewData.Error(result.error)
            }
        }
    }

    override suspend fun updateTaskCompletion(
        taskId: String,
        isCompleted: Boolean
    ): TaskResultViewData<Boolean> {
        return taskDataSource.updateTaskCompletion(taskId, isCompleted)
    }

    override suspend fun updateTask(task: TaskViewData): TaskResultViewData<Boolean> {
        if (task.id.isBlank()) return TaskResultViewData.Success(false)
        return taskDataSource.updateTask(task.id, task.toDto())
    }

    override suspend fun deleteTask(taskId: String): TaskResultViewData<Boolean> {
        return taskDataSource.deleteTask(taskId)
    }
}
