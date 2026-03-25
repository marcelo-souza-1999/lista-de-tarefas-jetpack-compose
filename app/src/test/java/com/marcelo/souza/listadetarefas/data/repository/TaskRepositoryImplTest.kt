package com.marcelo.souza.listadetarefas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {

    private val dataSource = mockk<TaskDataSource>()
    private val firebaseAuth = mockk<FirebaseAuth>()
    private val firebaseUser = mockk<FirebaseUser>()

    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup() {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "user_123"

        repository = TaskRepositoryImpl(dataSource, firebaseAuth)
    }


    @Test
    fun `saveTask should return Success when dataSource completes`() = runTest {
        val task = Task(title = "Task", description = "Desc", priority = TaskPriority.HIGH)
        coEvery { dataSource.saveTask(any()) } returns TaskResult.Success(true)

        val result = repository.saveTask(task)

        assertTrue(result is TaskResult.Success)
        coVerify { dataSource.saveTask(match { it.userId == "user_123" }) }
    }

    @Test
    fun `saveTask should return Permission error when user is null`() = runTest {
        every { firebaseAuth.currentUser } returns null

        val result = repository.saveTask(
            Task(
                title = "t",
                description = "description",
                priority = TaskPriority.LOW
            )
        )

        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `getTasks should map DTO list to Domain list`() = runTest {
        val dtoList = listOf(
            TaskDto(id = "1", title = "T1", priority = "HIGH"),
            TaskDto(id = "2", title = "T2", priority = "LOW")
        )
        every { dataSource.getTasks() } returns flowOf(TaskResult.Success(dtoList))

        val result = repository.getTasks().first()

        assertTrue(result is TaskResult.Success)
        val data = (result as TaskResult.Success).data
        assertEquals(2, data.size)
        assertEquals(TaskPriority.HIGH, data[0].priority)
        assertEquals(TaskPriority.LOW, data[1].priority)
    }

    @Test
    fun `getTasks should forward Error result`() = runTest {
        every { dataSource.getTasks() } returns flowOf(TaskResult.Error(DataError.Network()))

        val result = repository.getTasks().first()

        assertTrue(result is TaskResult.Error)
        assertTrue((result as TaskResult.Error).error is DataError.Network)
    }

    @Test
    fun `updateTaskCompletion should delegate call to dataSource`() = runTest {
        coEvery { dataSource.updateTaskCompletion("id", true) } returns TaskResult.Success(true)

        val result = repository.updateTaskCompletion("id", true)

        assertTrue(result is TaskResult.Success)
        coVerify { dataSource.updateTaskCompletion("id", true) }
    }

    @Test
    fun `updateTask should call dataSource when task id is valid`() = runTest {
        val task = Task(
            id = "valid_id",
            title = "Update",
            description = "Update Description",
            priority = TaskPriority.LOW
        )
        coEvery { dataSource.updateTask("valid_id", any()) } returns TaskResult.Success(true)

        val result = repository.updateTask(task)

        assertTrue(result is TaskResult.Success)
        coVerify { dataSource.updateTask("valid_id", any()) }
    }

    @Test
    fun `updateTask should return Success false when task id is blank`() = runTest {
        val task = Task(
            id = "", title = "No ID", description = "No Description",
            priority = TaskPriority.LOW
        )

        val result = repository.updateTask(task)

        assertEquals(TaskResult.Success(false), result)
        coVerify(exactly = 0) { dataSource.updateTask(any(), any()) }
    }

    @Test
    fun `updateTask should return Permission error when user is null`() = runTest {
        every { firebaseAuth.currentUser } returns null

        val result = repository.updateTask(
            Task(
                id = "1",
                title = "title",
                description = "description",
                priority = TaskPriority.LOW
            )
        )

        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `deleteTask should delegate call to dataSource`() = runTest {
        coEvery { dataSource.deleteTask("id_delete") } returns TaskResult.Success(true)

        val result = repository.deleteTask("id_delete")

        assertTrue(result is TaskResult.Success)
        coVerify { dataSource.deleteTask("id_delete") }
    }
}