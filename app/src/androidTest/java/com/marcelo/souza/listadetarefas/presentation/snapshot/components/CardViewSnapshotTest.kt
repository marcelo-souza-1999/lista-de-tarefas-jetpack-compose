package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardViewSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun taskCard_snapshot_priority_high_pending_light() {
        val task = Task("1", "Tarefa Urgente", "Finalizar o deploy hoje", TaskPriority.HIGH, false)
        runCardSnapshot(task, darkTheme = false, name = "task_card_high_light")
    }

    @Test
    fun taskCard_snapshot_priority_medium_pending_light() {
        val task = Task("2", "Reunião de Alinhamento", "Falar sobre os novos testes", TaskPriority.MEDIUM, false)
        runCardSnapshot(task, darkTheme = false, name = "task_card_medium_light")
    }

    @Test
    fun taskCard_snapshot_priority_low_pending_light() {
        val task = Task("3", "Comprar pão", "Não esquecer o integral", TaskPriority.LOW, false)
        runCardSnapshot(task, darkTheme = false, name = "task_card_low_light")
    }

    @Test
    fun taskCard_snapshot_completed_dark() {
        val task = Task("4", "Tarefa Finalizada", "Isso já foi feito", TaskPriority.HIGH, true)
        runCardSnapshot(task, darkTheme = true, name = "task_card_completed_dark")
    }

    @Test
    fun taskCard_snapshot_no_description_light() {
        val task = Task("5", "Tarefa sem descrição", "", TaskPriority.MEDIUM, false)
        runCardSnapshot(task, darkTheme = false, name = "task_card_no_desc_light")
    }

    private fun runCardSnapshot(task: Task, darkTheme: Boolean, name: String) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                TaskCard(
                    task = task,
                    onCheckedChange = {},
                    onEditClick = {},
                    onDeleteClick = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}
