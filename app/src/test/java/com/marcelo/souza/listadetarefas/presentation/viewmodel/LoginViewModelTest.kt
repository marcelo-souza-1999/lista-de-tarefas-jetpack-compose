package com.marcelo.souza.listadetarefas.presentation.viewmodel

import app.cash.turbine.test
import com.marcelo.souza.listadetarefas.utils.MainDispatcherRule
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mockk<AuthenticateRepository>()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `onEmailChange with invalid email should show error and disable form`() {
        viewModel.onEmailChange("marcelo.gmail")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_email_invalid_credentials, state.emailErrorResId)
        assertFalse(state.isFormValid)
    }

    @Test
    fun `onEmailChange with valid email should clear error and enable form if password is ok`() {
        viewModel.onPasswordChange("123456")
        viewModel.onEmailChange("marcelo@souza.com")

        val state = viewModel.uiState.value
        assertNull(state.emailErrorResId)
        assertTrue(state.isFormValid)
    }

    @Test
    fun `onPasswordChange with less than 6 chars should show weak password error`() {
        viewModel.onPasswordChange("12345")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_registration_weak_password, state.passwordErrorResId)
        assertFalse(state.isFormValid)
    }

    @Test
    fun `onTogglePasswordVisibility should switch boolean state`() {
        val initial = viewModel.uiState.value.isPasswordVisible
        viewModel.onTogglePasswordVisibility()
        assertEquals(!initial, viewModel.uiState.value.isPasswordVisible)
    }

    @Test
    fun `login should navigate to Home on success`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)
        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456")

        viewModel.navigationEvent.test {
            viewModel.login()

            assertEquals(NavigationEvent.NavigateAndClear(HomeKey), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login should navigate to Home when credentials are valid`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)

        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456")

        viewModel.navigationEvent.test {
            viewModel.login()

            assertEquals(NavigationEvent.NavigateAndClear(HomeKey), awaitItem())

            coVerify { authRepository.login("marcelo@souza.com", "123456") }
        }
    }

    @Test
    fun `login should NOT call repository if form is invalid`() = runTest {
        viewModel.onEmailChange("invalido")
        viewModel.login()

        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `login should NOT call repository if already loading`() = runTest {
        coEvery { authRepository.login(any(), any()) } coAnswers {
            kotlinx.coroutines.delay(1000)
            Result.success(Unit)
        }

        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456")

        launch { viewModel.login() }
        advanceTimeBy(100)

        viewModel.login()

        coVerify(exactly = 1) { authRepository.login(any(), any()) }
    }

    @Test
    fun `login failure should update errorResId and stop loading`() = runTest {
        val exception = Exception("Unauthorized")
        coEvery { authRepository.login(any(), any()) } returns Result.failure(exception)

        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456")

        viewModel.login()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorResId != null)
    }

    @Test
    fun `clearError should reset the global error state`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception())
        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456")
        viewModel.login()

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorResId)
    }
}