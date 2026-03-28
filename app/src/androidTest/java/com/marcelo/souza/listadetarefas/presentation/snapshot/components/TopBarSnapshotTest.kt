package com.marcelo.souza.listadetarefas.presentation.snapshot.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.components.SecondaryTopBar
import com.marcelo.souza.listadetarefas.presentation.ui.components.TopBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopBarSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun topBar_home_snapshot_light() {
        runTopBarSnapshot(darkTheme = false, name = "topbar_home_light") {
            TopBar(
                userName = "Marcelo Souza",
                onLogoutClick = {}
            )
        }
    }

    @Test
    fun topBar_home_snapshot_dark() {
        runTopBarSnapshot(darkTheme = true, name = "topbar_home_dark") {
            TopBar(
                userName = "Marcelo Souza",
                onLogoutClick = {}
            )
        }
    }

    @Test
    fun topBar_secondary_snapshot_light() {
        runTopBarSnapshot(darkTheme = false, name = "topbar_secondary_light") {
            SecondaryTopBar(
                title = "Nova Tarefa",
                onBackClick = {}
            )
        }
    }

    @Test
    fun topBar_secondary_snapshot_dark() {
        runTopBarSnapshot(darkTheme = true, name = "topbar_secondary_dark") {
            SecondaryTopBar(
                title = "Editar Tarefa",
                onBackClick = {}
            )
        }
    }

    private fun runTopBarSnapshot(
        darkTheme: Boolean,
        name: String,
        content: @Composable () -> Unit
    ) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                content()
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}