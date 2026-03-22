package com.marcelo.souza.listadetarefas.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.marcelo.souza.listadetarefas.R
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginUiState
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpUiState
import com.marcelo.souza.listadetarefas.presentation.ui.components.EmptyTasksState
import com.marcelo.souza.listadetarefas.presentation.ui.splash.SplashScreenContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeScreensAndComponentsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginScreenContent_shouldRenderMainActions() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithText(context.getString(R.string.login_button_primary_text)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.signup_button_secondary_text)).assertIsDisplayed()
    }

    @Test
    fun signUpScreenContent_shouldRenderMainActions() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                SignUpScreenContent(
                    uiState = SignUpUiState(),
                    onNameChange = {},
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onSignUpClick = {},
                    onNavigateToLogin = {}
                )
            }
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithText(context.getString(R.string.register_button_text)).assertIsDisplayed()
    }

    @Test
    fun components_shouldRenderTexts() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                EmptyTasksState(title = "Sem tarefas", description = "Adicione a primeira tarefa")
            }
        }

        composeRule.onNodeWithText("Sem tarefas").assertIsDisplayed()
        composeRule.onNodeWithText("Adicione a primeira tarefa").assertIsDisplayed()
    }

    @Test
    fun splashContent_shouldRenderAppName() {
        composeRule.setContent {
            ListaDeTarefasTheme {
                SplashScreenContent(alpha = 1f)
            }
        }

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeRule.onNodeWithText(context.getString(R.string.app_name)).assertIsDisplayed()
    }
}

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainActivity_shouldDisplayAppNameAtStartup() {
        activityRule.onNodeWithText(
            activityRule.activity.getString(R.string.app_name)
        ).assertIsDisplayed()
    }
}
