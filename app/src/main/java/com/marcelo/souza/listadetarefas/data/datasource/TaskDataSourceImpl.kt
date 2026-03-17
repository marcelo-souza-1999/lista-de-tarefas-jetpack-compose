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
            firestore.collection(COLLECTION_NAME).add(task.copy(id = "")).await()
            TaskResultViewData.Success(true)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun getTasks(): TaskResultViewData<List<TaskDto>> {
        return try {
            val snapshot = firestore.collection(COLLECTION_NAME).get().await()
            val tasks = snapshot.documents.mapNotNull { document ->
                document.toObject(TaskDto::class.java)?.copy(id = document.id)
            }
            TaskResultViewData.Success(tasks)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResultViewData<Boolean> {
        return try {
            firestore.collection(COLLECTION_NAME)
                .document(taskId)
                .update("isCompleted", isCompleted)
                .await()
            TaskResultViewData.Success(true)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    private fun mapFirebaseError(e: Exception): DataError {
        return when (e) {
            is FirebaseNetworkException -> DataError.Network()
            is FirebaseFirestoreException -> {
                when (e.code) {
                    FirebaseFirestoreException.Code.UNAUTHENTICATED,
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Permission()

                    FirebaseFirestoreException.Code.UNAVAILABLE,
                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> DataError.Network()

                    else -> DataError.Unknown()
                }
            }

            is FirebaseException -> DataError.Unknown()
            else -> DataError.Unknown()
        }
    }
}
