package com.marcelo.souza.listadetarefas.data.mapper

import com.marcelo.souza.listadetarefas.data.model.TaskDto
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskMapperTest {
    @Test
    fun `toDomain should map dto with valid priority`() {
        val dto = TaskDto(id = "1", userId = "user-1", title = "title", description = "desc", priority = "high", completed = true)

        val result = dto.toDomain()

        assertEquals("1", result.id)
        assertEquals(TaskPriority.HIGH, result.priority)
        assertEquals(true, result.isCompleted)
    }

    @Test
    fun `toDomain should fallback to LOW when priority is invalid`() {
        val result = TaskDto(priority = "invalid").toDomain()

        assertEquals(TaskPriority.LOW, result.priority)
    }

    @Test
    fun `toDto should map task and preserve user id`() {
        val task = Task(id = "10", title = "Task", description = "Description", priority = TaskPriority.MEDIUM, isCompleted = true)

        val result = task.toDto("user-10")

        assertEquals("10", result.id)
        assertEquals("user-10", result.userId)
        assertEquals("MEDIUM", result.priority)
    }
}
