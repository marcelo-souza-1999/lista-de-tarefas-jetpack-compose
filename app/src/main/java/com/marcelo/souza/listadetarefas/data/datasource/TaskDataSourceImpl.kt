package com.marcelo.souza.listadetarefas.data.datasource

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.data.utils.Constants.COLLECTION_NAME
import com.marcelo.souza.listadetarefas.data.utils.Constants.EMPTY_ID
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_IS_COMPLETED
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_USER_ID
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class TaskDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : TaskDataSource {

    override suspend fun saveTask(task: TaskDto): TaskResultViewData<Boolean> {
        val userId = firebaseAuth.currentUser?.uid ?: return TaskResultViewData.Error(DataError.Permission())

        return try {
            firestore.collection(COLLECTION_NAME)
                .add(task.copy(id = EMPTY_ID, userId = userId))
                .await()
            TaskResultViewData.Success(true)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun getTasks(): TaskResultViewData<List<TaskDto>> {
        val userId = firebaseAuth.currentUser?.uid ?: return TaskResultViewData.Error(DataError.Permission())

        return try {
            val snapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo(FIELD_USER_ID, userId)
                .get()
                .await()

            val tasks = snapshot.documents.mapNotNull { document ->
                document.toObject(TaskDto::class.java)?.copy(id = document.id)
            }
            TaskResultViewData.Success(tasks)
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun updateTaskCompletion(taskId: String, isCompleted: Boolean): TaskResultViewData<Boolean> {
        val userId = firebaseAuth.currentUser?.uid ?: return TaskResultViewData.Error(DataError.Permission())

        return try {
            val document = firestore.collection(COLLECTION_NAME).document(taskId)
            val task = document.get().await().toObject(TaskDto::class.java)
            if (task?.userId != userId) {
                TaskResultViewData.Error(DataError.Permission())
            } else {
                document.update(FIELD_IS_COMPLETED, isCompleted).await()
                TaskResultViewData.Success(true)
            }
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun updateTask(taskId: String, task: TaskDto): TaskResultViewData<Boolean> {
        val userId = firebaseAuth.currentUser?.uid ?: return TaskResultViewData.Error(DataError.Permission())

        return try {
            val document = firestore.collection(COLLECTION_NAME).document(taskId)
            val current = document.get().await().toObject(TaskDto::class.java)
            if (current?.userId != userId) {
                TaskResultViewData.Error(DataError.Permission())
            } else {
                document.set(task.copy(id = taskId, userId = userId)).await()
                TaskResultViewData.Success(true)
            }
        } catch (e: Exception) {
            TaskResultViewData.Error(mapFirebaseError(e))
        }
    }

    override suspend fun deleteTask(taskId: String): TaskResultViewData<Boolean> {
        val userId = firebaseAuth.currentUser?.uid ?: return TaskResultViewData.Error(DataError.Permission())

        return try {
            val document = firestore.collection(COLLECTION_NAME).document(taskId)
            val current = document.get().await().toObject(TaskDto::class.java)
            if (current?.userId != userId) {
                TaskResultViewData.Error(DataError.Permission())
            } else {
                document.delete().await()
                TaskResultViewData.Success(true)
            }
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
