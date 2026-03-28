package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.EmptyTasksState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyTasksStateSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyTasksState_snapshot_light() {
        runEmptyStateSnapshot(
            title = "Nenhuma tarefa encontrada",
            description = "Que tal começar o dia criando uma nova tarefa no botão abaixo?",
            darkTheme = false,
            name = "empty_state_light"
        )
    }

    @Test
    fun emptyTasksState_snapshot_dark() {
        runEmptyStateSnapshot(
            title = "Tudo limpo por aqui!",
            description = "Aproveite seu tempo livre ou planeje o próximo desafio.",
            darkTheme = true,
            name = "empty_state_dark"
        )
    }

    @Test
    fun emptyTasksState_snapshot_long_text_light() {
        runEmptyStateSnapshot(
            title = "Título muito grande para testar o limite de quebra de linha do componente",
            description = "Uma descrição igualmente longa para verificar se o componente mantém o " +
                "alinhamento centralizado mesmo com muito conteúdo visual na tela.",
            darkTheme = false,
            name = "empty_state_long_text_light"
        )
    }

    private fun runEmptyStateSnapshot(
        title: String,
        description: String,
        darkTheme: Boolean,
        name: String
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                EmptyTasksState(
                    title = title,
                    description = description
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}
