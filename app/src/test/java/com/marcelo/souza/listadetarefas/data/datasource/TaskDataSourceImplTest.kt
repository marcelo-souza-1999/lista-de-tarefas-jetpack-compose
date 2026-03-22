package com.marcelo.souza.listadetarefas.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TaskDataSourceImplTest {
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val authRepository = mockk<AuthenticateRepository>()

    private lateinit var dataSource: TaskDataSourceImpl

    @Before
    fun setup() {
        dataSource = TaskDataSourceImpl(firestore, authRepository)
    }

    @Test
    fun `saveTask should return permission when user is null`() = runTest {
        every { authRepository.getCurrentUserId() } returns null

        val result = dataSource.saveTask(TaskDto(title = "t"))

        assertTrue(result is TaskResult.Error)
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `updateTask should return permission when user is null`() = runTest {
        every { authRepository.getCurrentUserId() } returns null

        val result = dataSource.updateTask("id", TaskDto(title = "t"))

        assertTrue(result is TaskResult.Error)
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }

    @Test
    fun `getTasks should emit permission when user is null`() = runTest {
        every { authRepository.getCurrentUserId() } returns null

        val result = dataSource.getTasks().first()

        assertTrue(result is TaskResult.Error)
        assertTrue((result as TaskResult.Error).error is DataError.Permission)
    }
}
