package com.marcelo.souza.listadetarefas.presentation.snapshot.authenticate

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpScreenContent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val defaultState = SignUpUiState(
        name = "",
        email = "",
        password = "",
        isPasswordVisible = false,
        isLoading = false,
        isFormValid = false
    )

    private val validState = defaultState.copy(
        name = "Marcelo Souza",
        email = "marcelo@desenvolvedor.com",
        password = "SenhaForte123",
        isFormValid = true
    )

    private val errorState = defaultState.copy(
        name = "",
        email = "email_invalido",
        password = "123",
        emailErrorResId = R.string.error_email_invalid_credentials,
        passwordErrorResId = R.string.error_registration_weak_password
    )

    private val loadingState = validState.copy(
        isLoading = true
    )

    @Test
    fun signUpScreen_snapshot_light_default() {
        runSnapshotTest(defaultState, darkTheme = false, "signup_light_default")
    }

    @Test
    fun signUpScreen_snapshot_light_error() {
        runSnapshotTest(errorState, darkTheme = false, "signup_light_error")
    }

    @Test
    fun signUpScreen_snapshot_light_loading() {
        runSnapshotTest(loadingState, darkTheme = false, "signup_light_loading")
    }

    @Test
    fun signUpScreen_snapshot_dark_default() {
        runSnapshotTest(defaultState, darkTheme = true, "signup_dark_default")
    }

    @Test
    fun signUpScreen_snapshot_dark_error() {
        runSnapshotTest(errorState, darkTheme = true, "signup_dark_error")
    }

    @Test
    fun signUpScreen_snapshot_dark_loading() {
        runSnapshotTest(loadingState, darkTheme = true, "signup_dark_loading")
    }

    @Test
    fun signUpScreen_snapshot_password_visible() {
        val state = validState.copy(isPasswordVisible = true)
        runSnapshotTest(state, darkTheme = false, "signup_password_visible")
    }

    private fun runSnapshotTest(state: SignUpUiState, darkTheme: Boolean, fileName: String) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                SignUpScreenContent(
                    uiState = state,
                    onNameChange = {},
                    onEmailChange = {},
                    onPasswordChange = {},
                    onTogglePasswordVisibility = {},
                    onSignUpClick = {},
                    onNavigateToLogin = {}
                )
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, fileName)
    }
}