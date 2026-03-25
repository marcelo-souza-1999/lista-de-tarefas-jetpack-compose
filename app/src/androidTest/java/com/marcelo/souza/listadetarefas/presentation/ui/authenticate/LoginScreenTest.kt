package com.marcelo.souza.listadetarefas.presentation.ui.authenticate

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.model.RoutesKey
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.viewmodel.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var uiStateFlow: MutableStateFlow<LoginUiState>
    private lateinit var navigationEventFlow: MutableSharedFlow<NavigationEvent>
    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)

    @Before
    fun setup() {
        uiStateFlow = MutableStateFlow(LoginUiState(email = "", password = ""))
        navigationEventFlow = MutableSharedFlow()

        every { mockViewModel.uiState } returns uiStateFlow
        every { mockViewModel.navigationEvent } returns navigationEventFlow
    }

    private fun setContent(
        onNavigateToRegister: () -> Unit = {},
        onLoginSuccess: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                LoginScreen(
                    onNavigateToRegister = onNavigateToRegister,
                    onLoginSuccess = onLoginSuccess,
                    viewModel = mockViewModel
                )
            }
        }
    }

    @Test
    fun verifyInitialElementsAreDisplayed() {
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.login_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_email)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_password))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.login_button_primary_text))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.signup_button_secondary_text))
            .assertIsDisplayed()
    }

    @Test
    fun loginButtonIsDisabledWhenFormIsInvalid() {
        uiStateFlow.value = LoginUiState(isFormValid = false)

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.login_button_primary_text))
            .assertIsNotEnabled()
    }

    @Test
    fun whenTypeInEmailFieldCallsViewModelOnEmailChange() {
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.label_email))
            .performTextInput("teste@email.com")

        verify { mockViewModel.onEmailChange("teste@email.com") }
    }

    @Test
    fun whenTypeInPasswordFieldCallsViewModelOnPasswordChange() {
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.label_password))
            .performTextInput("senha123")

        verify { mockViewModel.onPasswordChange("senha123") }
    }

    @Test
    fun whenClickRegisterButtonCallsOnNavigateToRegister() {
        var navigated = false
        setContent(onNavigateToRegister = { navigated = true })

        composeTestRule.onNodeWithText(context.getString(R.string.signup_button_secondary_text))
            .performClick()

        assert(navigated)
    }

    @Test
    fun whenClickLoginButtonCallsViewModelLogin() {
        uiStateFlow.value = LoginUiState(isFormValid = true)
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.login_button_primary_text))
            .performClick()

        verify { mockViewModel.login() }
    }

    @Test
    fun whenEmailHasErrorDisplaysErrorMessage() {
        val errorId = R.string.label_email
        uiStateFlow.value = LoginUiState(emailErrorResId = errorId)

        setContent()

        composeTestRule.onNodeWithTag("error_message_input_text")
            .assertIsDisplayed()
            .assertTextEquals(context.getString(errorId))
    }

    @Test
    fun whenErrorResIdIsNotNullDisplaysDialogAndRetryCallsViewModel() {
        val errorMsgId = R.string.error_login_invalid_credentials
        uiStateFlow.value = LoginUiState(errorResId = errorMsgId)

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.title_error_dialog_auth))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(errorMsgId)).assertIsDisplayed()

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        verify(exactly = 1) { mockViewModel.clearError() }
        verify(exactly = 1) { mockViewModel.login() }
    }

    @Test
    fun whenNavigationEventIsNavigateAndClearCallsOnLoginSuccess() = runTest {
        var successCalled = false

        setContent(onLoginSuccess = { successCalled = true })

        val routeMock = mockk<RoutesKey>(relaxed = true)

        navigationEventFlow.emit(NavigationEvent.NavigateAndClear(routeMock))

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            successCalled
        }

        assertTrue("A lambda onLoginSuccess deveria ter sido chamada", successCalled)
    }
}