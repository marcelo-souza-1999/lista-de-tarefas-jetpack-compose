package com.marcelo.souza.listadetarefas.data.repository

import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
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
        // 1. O Repository recebe um objeto de domínio (TaskViewData)
        // 2. Usamos o mapper para converter para TaskDto (o formato que o Firebase conhece)
        val taskDto = task.toDto()

        // 3. Chamamos o DataSource para realizar a operação real no banco
        // O DataSource já nos devolve o resultado tratado com Sucesso ou Erro
        return taskDataSource.saveTask(taskDto)
    }

    override suspend fun getTasks(): TaskResultViewData<List<TaskViewData>> {
        // Implementaremos em breve
        return TaskResultViewData.Success(emptyList())
    }
}