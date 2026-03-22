package com.marcelo.souza.listadetarefas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marcelo.souza.listadetarefas.data.datasource.TaskDataSource
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
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
        repository = TaskRepositoryImpl(dataSource, firebaseAuth)
    }

    @Test
    fun `saveTask should return permission error when user is null`() = runTest {
        every { firebaseAuth.currentUser } returns null

        val result = repository.saveTask(Task(title = "t", description = "d", priority = TaskPriority.LOW))

        assertTrue(result is TaskResult.Error)
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `updateTask should return false when task id is blank`() = runTest {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "uid"

        val result = repository.updateTask(Task(id = "", title = "t", description = "d", priority = TaskPriority.LOW))

        assertEquals(TaskResult.Success(false), result)
    }

    @Test
    fun `getTasks should map dto to domain`() = runTest {
        every { dataSource.getTasks() } returns flowOf(
            TaskResult.Success(
                listOf(
                    com.marcelo.souza.listadetarefas.data.model.TaskDto(
                        id = "1",
                        title = "Task",
                        description = "Desc",
                        priority = "HIGH",
                        completed = true
                    )
                )
            )
        )

        val result = repository.getTasks().first()

        assertTrue(result is TaskResult.Success)
        assertEquals("1", (result as TaskResult.Success).data.first().id)
    }
}
