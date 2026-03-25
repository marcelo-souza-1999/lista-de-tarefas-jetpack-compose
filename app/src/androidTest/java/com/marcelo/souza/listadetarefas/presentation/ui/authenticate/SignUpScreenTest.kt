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
import com.marcelo.souza.listadetarefas.presentation.viewmodel.SignUpViewModel
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
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var uiStateFlow: MutableStateFlow<SignUpUiState>
    private lateinit var navigationEventFlow: MutableSharedFlow<NavigationEvent>
    private val mockViewModel = mockk<SignUpViewModel>(relaxed = true)

    @Before
    fun setup() {
        uiStateFlow = MutableStateFlow(SignUpUiState())
        navigationEventFlow = MutableSharedFlow()

        every { mockViewModel.uiState } returns uiStateFlow
        every { mockViewModel.navigationEvent } returns navigationEventFlow
    }

    private fun setContent(
        onNavigateToLogin: () -> Unit = {},
        onSignUpSuccess: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                SignUpScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onSignUpSuccess = onSignUpSuccess,
                    viewModel = mockViewModel
                )
            }
        }
    }

    @Test
    fun verifyInitialElementsAreDisplayed() {
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.register_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_name)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_email)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.label_password)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.register_button_text)).assertIsDisplayed()
    }

    @Test
    fun signUpButtonIsDisabledWhenFormIsInvalid() {
        uiStateFlow.value = SignUpUiState(isFormValid = false)
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.register_button_text))
            .assertIsNotEnabled()
    }

    @Test
    fun whenTypeInFieldsCallsViewModelObservers() {
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.label_name)).performTextInput("Marcelo Souza")
        verify { mockViewModel.onNameChange("Marcelo Souza") }

        composeTestRule.onNodeWithText(context.getString(R.string.label_email)).performTextInput("marcelo@teste.com")
        verify { mockViewModel.onEmailChange("marcelo@teste.com") }

        composeTestRule.onNodeWithText(context.getString(R.string.label_password)).performTextInput("123456")
        verify { mockViewModel.onPasswordChange("123456") }
    }

    @Test
    fun whenClickSignUpButtonCallsViewModelSignUp() {
        uiStateFlow.value = SignUpUiState(isFormValid = true)
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.register_button_text)).performClick()
        verify { mockViewModel.signUp() }
    }

    @Test
    fun whenClickLoginLinkCallsOnNavigateToLogin() {
        var navigated = false
        setContent(onNavigateToLogin = { navigated = true })

        composeTestRule.onNodeWithText(context.getString(R.string.login_link_text)).performClick()
        assertTrue(navigated)
    }

    @Test
    fun whenNameHasErrorDisplaysErrorMessage() {
        val errorId = R.string.label_name
        uiStateFlow.value = SignUpUiState(nameErrorResId = errorId)

        setContent()

        composeTestRule.onNodeWithTag("error_message_input_text")
            .assertIsDisplayed()
            .assertTextEquals(context.getString(errorId))
    }

    @Test
    fun whenErrorResIdNotNullDisplaysDialogAndRetryCallsSignUp() {
        val errorMsgId = R.string.error_login_invalid_credentials
        uiStateFlow.value = SignUpUiState(errorResId = errorMsgId)

        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.title_error_dialog_auth)).assertIsDisplayed()

        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        verify(exactly = 1) { mockViewModel.clearError() }
        verify(exactly = 1) { mockViewModel.signUp() }
    }

    @Test
    fun whenNavigationEventIsNavigateAndClearCallsOnSignUpSuccess() = runTest {
        var successCalled = false
        setContent(onSignUpSuccess = { successCalled = true })

        val routeMock = mockk<RoutesKey>(relaxed = true)
        navigationEventFlow.emit(NavigationEvent.NavigateAndClear(routeMock))

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            successCalled
        }

        assertTrue("A lambda onSignUpSuccess deveria ter sido chamada", successCalled)
    }
}