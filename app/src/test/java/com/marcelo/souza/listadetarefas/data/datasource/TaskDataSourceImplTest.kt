package com.marcelo.souza.listadetarefas.data.datasource

import app.cash.turbine.test
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.data.utils.Constants.COLLECTION_NAME
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDataSourceImplTest {

    private val firestore = mockk<FirebaseFirestore>()
    private val authRepository = mockk<AuthenticateRepository>()
    private val collectionReference = mockk<CollectionReference>()
    private val documentReference = mockk<DocumentReference>()
    private val query = mockk<Query>()

    private lateinit var dataSource: TaskDataSourceImpl

    @Before
    fun setup() {
        every { firestore.collection(COLLECTION_NAME) } returns collectionReference
        every { collectionReference.document(any()) } returns documentReference
        dataSource = TaskDataSourceImpl(firestore, authRepository)
    }


    @Test
    fun `saveTask should return Success when firebase adds document`() = runTest {
        val taskDto = TaskDto(title = "Task Test")
        every { authRepository.getCurrentUserId() } returns "user123"

        val tcs = TaskCompletionSource<DocumentReference>()
        tcs.setResult(documentReference)
        every { collectionReference.add(any()) } returns tcs.task

        val result = dataSource.saveTask(taskDto)

        assertTrue(result is TaskResult.Success)
        verify { collectionReference.add(match<TaskDto> { it.userId == "user123" }) }
    }

    @Test
    fun `saveTask should return Permission error when userId is null`() = runTest {
        every { authRepository.getCurrentUserId() } returns null
        val result = dataSource.saveTask(TaskDto(title = "t"))
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `updateTaskCompletion should return Success`() = runTest {
        val tcs = TaskCompletionSource<Void>()
        tcs.setResult(null)
        every { documentReference.update(any<String>(), any()) } returns tcs.task

        val result = dataSource.updateTaskCompletion("id1", true)

        assertTrue(result is TaskResult.Success)
        verify { documentReference.update(any<String>(), true) }
    }

    @Test
    fun `updateTask should return Success and set correct userId`() = runTest {
        every { authRepository.getCurrentUserId() } returns "user123"

        val tcs = TaskCompletionSource<Void>()
        tcs.setResult(null)
        every { documentReference.set(any()) } returns tcs.task

        val result = dataSource.updateTask("id1", TaskDto(title = "Editada"))

        assertTrue(result is TaskResult.Success)
        verify {
            documentReference.set(match<TaskDto> {
                it.id == "id1" && it.userId == "user123"
            })
        }
    }

    @Test
    fun `deleteTask should return Success when document is deleted`() = runTest {
        val tcs = TaskCompletionSource<Void>()
        tcs.setResult(null)
        every { documentReference.delete() } returns tcs.task

        val result = dataSource.deleteTask("id123")

        assertTrue(result is TaskResult.Success)
        verify { documentReference.delete() }
    }

    @Test
    fun `getTasks should emit Success list and terminate`() = runTest {
        val userId = "user123"
        every { authRepository.getCurrentUserId() } returns userId
        every { collectionReference.whereEqualTo(any<String>(), userId) } returns query

        val slot = slot<EventListener<QuerySnapshot>>()
        every { query.addSnapshotListener(capture(slot)) } returns mockk {
            every { remove() } returns Unit
        }

        dataSource.getTasks().test {
            val mockSnapshot = mockk<QuerySnapshot>()
            val mockDoc = mockk<QueryDocumentSnapshot>()
            every { mockSnapshot.documents } returns listOf(mockDoc)
            every { mockDoc.id } returns "docId"
            every { mockDoc.toObject(TaskDto::class.java) } returns TaskDto(title = "Teste")

            slot.captured.onEvent(mockSnapshot, null)

            val result = awaitItem()
            assertTrue(result is TaskResult.Success)
            assertEquals("Teste", (result as TaskResult.Success).data[0].title)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `mapFirebaseError should return Network when FirebaseNetworkException occurs`() = runTest {
        every { authRepository.getCurrentUserId() } returns "123"
        every { collectionReference.add(any()) } throws mockk<FirebaseNetworkException>()

        val result = dataSource.saveTask(TaskDto(title = "t"))
        assertTrue((result as TaskResult.Error).error is DataError.Network)
    }

    @Test
    fun `mapFirebaseError should return Permission when Firestore code is PERMISSION_DENIED`() = runTest {
        every { authRepository.getCurrentUserId() } returns "123"

        val exception = mockk<FirebaseFirestoreException>()
        every { exception.code } returns FirebaseFirestoreException.Code.PERMISSION_DENIED

        every { collectionReference.add(any()) } throws exception

        val result = dataSource.saveTask(TaskDto(title = "t"))
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `mapFirebaseError should return Network when Firestore code is UNAVAILABLE`() = runTest {
        every { authRepository.getCurrentUserId() } returns "123"

        val exception = mockk<FirebaseFirestoreException>()
        every { exception.code } returns FirebaseFirestoreException.Code.UNAVAILABLE

        every { collectionReference.add(any()) } throws exception

        val result = dataSource.saveTask(TaskDto(title = "t"))

        assertTrue((result as TaskResult.Error).error is DataError.Network)
    }

    @Test
    fun `mapFirebaseError should return Unknown for generic exceptions`() = runTest {
        every { authRepository.getCurrentUserId() } returns "123"
        every { collectionReference.add(any()) } throws RuntimeException("Explodiu")

        val result = dataSource.saveTask(TaskDto(title = "t"))
        assertTrue((result as TaskResult.Error).error is DataError.Unknown)
    }
}