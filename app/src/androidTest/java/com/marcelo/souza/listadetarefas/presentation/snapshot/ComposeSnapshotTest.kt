package com.marcelo.souza.listadetarefas.presentation.snapshot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginUiState
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpUiState
import com.marcelo.souza.listadetarefas.presentation.ui.components.EmptyTasksState
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskCard
import com.marcelo.souza.listadetarefas.presentation.ui.splash.SplashScreenContent
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginScreen_snapshot() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                LoginScreenContent(
                    uiState = LoginUiState(email = "user@mail.com", isFormValid = true),
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }

        compareScreenshot(composeRule)
    }

    @Test
    fun signUpScreen_snapshot() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                SignUpScreenContent(
                    uiState = SignUpUiState(name = "Marcelo", email = "user@mail.com", isFormValid = true),
                    onNameChange = {},
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onSignUpClick = {},
                    onNavigateToLogin = {}
                )
            }
        }

        compareScreenshot(composeRule)
    }

    @Test
    fun components_snapshot() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                TaskCard(
                    task = Task(
                        id = "1",
                        title = "Task snapshot",
                        description = "Task component",
                        priority = TaskPriority.HIGH,
                        isCompleted = false
                    ),
                    onCheckedChange = {},
                    onEditClick = {},
                    onDeleteClick = {}
                )
            }
        }

        compareScreenshot(composeRule)
    }

    @Test
    fun splash_snapshot() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                SplashScreenContent(alpha = 1f)
            }
        }

        compareScreenshot(composeRule)
    }

    @Test
    fun emptyState_snapshot() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                EmptyTasksState(title = "Sem tarefas", description = "Adicione sua primeira tarefa")
            }
        }

        compareScreenshot(composeRule)
    }
}
