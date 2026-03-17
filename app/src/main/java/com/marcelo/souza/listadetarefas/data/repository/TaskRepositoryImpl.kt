package com.marcelo.souza.listadetarefas.data.repository

import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
import com.marcelo.souza.listadetarefas.data.mapper.toDomain
import com.marcelo.souza.listadetarefas.data.mapper.toDto
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import org.koin.core.annotation.Single

@Single
class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {

    override suspend fun saveTask(task: TaskViewData): TaskResultViewData<Boolean> {
        return taskDataSource.saveTask(task.toDto())
    }

    override suspend fun getTasks(): TaskResultViewData<List<TaskViewData>> {
        return when (val result = taskDataSource.getTasks()) {
            is TaskResultViewData.Success -> {
                TaskResultViewData.Success(result.data.map { it.toDomain() })
            }

            is TaskResultViewData.Error -> TaskResultViewData.Error(result.error)
        }
    }

    override suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResultViewData<Boolean> {
        return taskDataSource.updateTaskCompletion(taskId, isCompleted)
    }
}
