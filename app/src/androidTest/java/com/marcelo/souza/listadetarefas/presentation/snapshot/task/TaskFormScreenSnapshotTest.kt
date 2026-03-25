package com.marcelo.souza.listadetarefas.presentation.snapshot.task

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.task.TaskFormContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskFormScreenSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun taskForm_snapshot_create_light_empty() {
        runTaskFormSnapshot(
            title = "",
            description = "",
            isEditing = false,
            darkTheme = false,
            name = "task_form_create_light_empty"
        )
    }

    @Test
    fun taskForm_snapshot_create_light_priority_high() {
        runTaskFormSnapshot(
            title = "Estudar para o exame",
            description = "Revisar Kotlin e Compose",
            priority = TaskPriority.HIGH,
            isEditing = false,
            darkTheme = false,
            name = "task_form_create_light_priority_high"
        )
    }

    @Test
    fun taskForm_snapshot_edit_dark_filled() {
        runTaskFormSnapshot(
            title = "Academia (Editando)",
            description = "Treino de hoje pago!",
            priority = TaskPriority.MEDIUM,
            isEditing = true,
            darkTheme = true,
            name = "task_form_edit_dark_filled"
        )
    }

    @Test
    fun taskForm_snapshot_save_loading() {
        runTaskFormSnapshot(
            title = "Finalizando...",
            description = "Aguardando resposta da API",
            isLoading = true,
            isEditing = false,
            darkTheme = false,
            name = "task_form_save_loading"
        )
    }

    private fun runTaskFormSnapshot(
        title: String,
        description: String,
        priority: TaskPriority = TaskPriority.LOW,
        isEditing: Boolean,
        isLoading: Boolean = false,
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                val titleTopBar = if (isEditing) "Editar Tarefa" else "Nova Tarefa"
                val titleButton = if (isEditing) "Salvar Alterações" else "Cadastrar Tarefa"

                TaskFormContent(
                    title = title,
                    titleTopBar = titleTopBar,
                    titleButton = titleButton,
                    onTitleChange = {},
                    description = description,
                    onDescriptionChange = {},
                    selectedTaskPriority = priority,
                    onPrioritySelect = {},
                    onSaveClick = {},
                    onBackClick = {},
                    isLoading = isLoading,
                    isSaveButtonEnabled = title.isNotBlank() && !isLoading
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}