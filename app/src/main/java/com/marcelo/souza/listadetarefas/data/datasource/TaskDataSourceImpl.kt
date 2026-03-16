package com.marcelo.souza.listadetarefas.data.datasource

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.data.utils.Constants.COLLECTION_NAME
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class TaskDataSourceImpl(
    private val firestore: FirebaseFirestore
) : TaskDataSource {
    override suspend fun saveTask(task: TaskDto): TaskResultViewData<Boolean> {
        return try {
            firestore.collection(COLLECTION_NAME).add(task).await()
            TaskResultViewData.Success(true)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun getTasks(): List<TaskDto> {
        // Implementação para buscar a lista no Firestore
        return emptyList() // Implementaremos em breve!
    }

    private fun mapFirebaseError(e: Exception): DataError {
        return when (e) {
            is FirebaseNetworkException -> DataError.Network()

            is FirebaseFirestoreException -> {
                when (e.code) {
                    FirebaseFirestoreException.Code.UNAUTHENTICATED -> DataError.Permission()
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Permission()
                    FirebaseFirestoreException.Code.UNAVAILABLE -> DataError.Network()
                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> DataError.Network()
                    else -> DataError.Unknown()
                }
            }

            is FirebaseException -> DataError.Unknown()

            else -> DataError.Unknown()
        }
    }
}