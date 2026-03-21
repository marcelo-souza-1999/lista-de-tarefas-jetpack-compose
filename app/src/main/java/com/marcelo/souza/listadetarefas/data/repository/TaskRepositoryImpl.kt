package com.marcelo.souza.listadetarefas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
import com.marcelo.souza.listadetarefas.data.mapper.toDomain
import com.marcelo.souza.listadetarefas.data.mapper.toDto
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val firebaseAuth: FirebaseAuth
) : TaskRepository {

    override suspend fun saveTask(task: Task): TaskResult<Boolean> {
        val userId = firebaseAuth.currentUser?.uid
            ?: return TaskResult.Error(DataError.Permission())

        return taskDataSource.saveTask(task.toDto(userId))
    }

    override fun getTasks(): Flow<TaskResult<List<Task>>> {
        return taskDataSource.getTasks().map { result ->
            when (result) {
                is TaskResult.Success -> {
                    TaskResult.Success(result.data.map { it.toDomain() })
                }

                is TaskResult.Error -> {
                    TaskResult.Error(result.error)
                }
            }
        }
    }

    override suspend fun updateTaskCompletion(
        taskId: String,
        isCompleted: Boolean
    ): TaskResult<Boolean> {
        return taskDataSource.updateTaskCompletion(taskId, isCompleted)
    }

    override suspend fun updateTask(task: Task): TaskResult<Boolean> {
        val userId = firebaseAuth.currentUser?.uid
            ?: return TaskResult.Error(DataError.Permission())

        if (task.id.isBlank()) return TaskResult.Success(false)

        return taskDataSource.updateTask(
            task.id,
            task.toDto(userId)
        )
    }

    override suspend fun deleteTask(taskId: String): TaskResult<Boolean> {
        return taskDataSource.deleteTask(taskId)
    }
}