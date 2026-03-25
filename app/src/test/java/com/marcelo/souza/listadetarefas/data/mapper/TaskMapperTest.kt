package com.marcelo.souza.listadetarefas.data.mapper

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskMapperTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = TaskDto(
            id = "1",
            userId = "user-1",
            title = "Estudar Kotlin",
            description = "Focar em Mappers",
            priority = "medium",
            completed = true
        )

        val result = dto.toDomain()

        assertEquals("1", result.id)
        assertEquals("Estudar Kotlin", result.title)
        assertEquals("Focar em Mappers", result.description)
        assertEquals(TaskPriority.MEDIUM, result.priority)
        assertEquals(true, result.isCompleted)
    }

    @Test
    fun `toDomain should map all priority levels correctly`() {
        assertEquals(TaskPriority.LOW, TaskDto(priority = "low").toDomain().priority)
        assertEquals(TaskPriority.MEDIUM, TaskDto(priority = "medium").toDomain().priority)
        assertEquals(TaskPriority.HIGH, TaskDto(priority = "high").toDomain().priority)
    }

    @Test
    fun `toDomain should fallback to LOW when priority is invalid or empty`() {
        val invalidResult = TaskDto(priority = "URGENT").toDomain()
        val emptyResult = TaskDto(priority = "").toDomain()

        assertEquals(TaskPriority.LOW, invalidResult.priority)
        assertEquals(TaskPriority.LOW, emptyResult.priority)
    }

    @Test
    fun `toDto should map domain to data transfer object correctly`() {
        val task = Task(
            id = "abc",
            title = "Título",
            description = "Desc",
            priority = TaskPriority.HIGH,
            isCompleted = false
        )

        val result = task.toDto("userId_999")

        assertEquals("abc", result.id)
        assertEquals("userId_999", result.userId)
        assertEquals("HIGH", result.priority)
        assertEquals(false, result.completed)
    }

    @Test
    fun `toDto should handle empty strings in title or description`() {
        val task = Task(
            id = "",
            title = "",
            description = "",
            priority = TaskPriority.LOW,
            isCompleted = false
        )

        val result = task.toDto("user-0")

        assertEquals("", result.title)
        assertEquals("", result.description)
    }
}