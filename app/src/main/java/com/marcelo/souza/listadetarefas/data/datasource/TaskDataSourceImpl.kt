package com.marcelo.souza.listadetarefas.data.datasource

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.data.utils.Constants.COLLECTION_NAME
import com.marcelo.souza.listadetarefas.data.utils.Constants.EMPTY_ID
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_IS_COMPLETED
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_USER_ID
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class TaskDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthenticateRepository
) : TaskDataSource {

    override suspend fun saveTask(task: TaskDto): TaskResult<Boolean> {
        val userId = authRepository.getCurrentUserId()
            ?: return TaskResult.Error(DataError.Permission())

        return try {
            firestore.collection(COLLECTION_NAME)
                .add(task.copy(id = EMPTY_ID, userId = userId))
                .await()

            TaskResult.Success(true)
        } catch (e: Exception) {
            TaskResult.Error(mapFirebaseError(e))
        }
    }

    override fun getTasks(): Flow<TaskResult<List<TaskDto>>> = callbackFlow {
        val userId = authRepository.getCurrentUserId()

        if (userId == null) {
            trySend(TaskResult.Error(DataError.Permission()))
            close()
            return@callbackFlow
        }

        val listener = firestore.collection(COLLECTION_NAME)
            .whereEqualTo(FIELD_USER_ID, userId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(TaskResult.Error(mapFirebaseError(error)))
                    return@addSnapshotListener
                }

                val tasks = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(TaskDto::class.java)?.copy(id = doc.id)
                }.orEmpty()

                trySend(TaskResult.Success(tasks))
            }

        awaitClose { listener.remove() }
    }

    override suspend fun updateTaskCompletion(
        taskId: String,
        isCompleted: Boolean
    ): TaskResult<Boolean> {

        return try {
            firestore.collection(COLLECTION_NAME)
                .document(taskId)
                .update(FIELD_IS_COMPLETED, isCompleted)
                .await()

            TaskResult.Success(true)
        } catch (e: Exception) {
            TaskResult.Error(mapFirebaseError(e))
        }
    }

    override suspend fun updateTask(
        taskId: String,
        task: TaskDto
    ): TaskResult<Boolean> {

        val userId = authRepository.getCurrentUserId()
            ?: return TaskResult.Error(DataError.Permission())

        return try {
            firestore.collection(COLLECTION_NAME)
                .document(taskId)
                .set(task.copy(id = taskId, userId = userId))
                .await()

            TaskResult.Success(true)
        } catch (e: Exception) {
            TaskResult.Error(mapFirebaseError(e))
        }
    }

    override suspend fun deleteTask(taskId: String): TaskResult<Boolean> {
        return try {
            firestore.collection(COLLECTION_NAME)
                .document(taskId)
                .delete()
                .await()

            TaskResult.Success(true)
        } catch (e: Exception) {
            TaskResult.Error(mapFirebaseError(e))
        }
    }

    private fun mapFirebaseError(e: Exception): DataError {
        return when (e) {
            is FirebaseNetworkException -> DataError.Network()

            is FirebaseFirestoreException -> when (e.code) {
                FirebaseFirestoreException.Code.UNAUTHENTICATED,
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> DataError.Permission()

                FirebaseFirestoreException.Code.UNAVAILABLE,
                FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> DataError.Network()

                else -> DataError.Unknown()
            }

            is FirebaseException -> DataError.Unknown()
            else -> DataError.Unknown()
        }
    }
}