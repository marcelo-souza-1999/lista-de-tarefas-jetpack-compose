package com.marcelo.souza.listadetarefas.presentation.snapshot.authenticate

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultState = LoginUiState(
        email = "",
        password = "",
        isPasswordVisible = false,
        isLoading = false,
        isFormValid = false,
        emailErrorResId = null,
        passwordErrorResId = null
    )

    private val validState = defaultState.copy(
        email = "marcelo@teste.com",
        password = "senhaSegura123",
        isFormValid = true
    )

    private val errorState = defaultState.copy(
        email = "marcelo@",
        password = "123",
        emailErrorResId = R.string.error_email_invalid_credentials,
        passwordErrorResId = R.string.error_registration_weak_password
    )

    private val loadingState = validState.copy(
        isLoading = true
    )

    private val passwordVisibleState = validState.copy(
        isPasswordVisible = true
    )

    @Test
    fun loginScreen_snapshot_light_theme_default() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = defaultState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_light_default")
    }

    @Test
    fun loginScreen_snapshot_dark_theme_default() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = true) {
                LoginScreenContent(
                    uiState = defaultState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_dark_default")
    }

    @Test
    fun loginScreen_snapshot_valid_state() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = validState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_valid_state")
    }

    @Test
    fun loginScreen_snapshot_error_state() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = errorState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_error_state")
    }

    @Test
    fun loginScreen_snapshot_loading_state() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = loadingState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_loading_state")
    }

    @Test
    fun loginScreen_snapshot_password_visible() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = false) {
                LoginScreenContent(
                    uiState = passwordVisibleState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_password_visible")
    }

    @Test
    fun loginScreen_snapshot_dark_theme_error_state() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = true) {
                LoginScreenContent(
                    uiState = errorState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_dark_error_state")
    }

    @Test
    fun loginScreen_snapshot_dark_theme_loading_state() {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = true) {
                LoginScreenContent(
                    uiState = loadingState,
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onLoginClick = {},
                    onNavigateToRegister = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, "login_screen_dark_loading_state")
    }
}