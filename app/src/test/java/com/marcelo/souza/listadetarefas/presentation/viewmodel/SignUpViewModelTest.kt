package com.marcelo.souza.listadetarefas.presentation.viewmodel

import app.cash.turbine.test
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [33])
@RunWith(RobolectricTestRunner::class)
class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mockk<AuthenticateRepository>(relaxed = true)
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        viewModel = SignUpViewModel(authRepository)
    }

    @Test
    fun `validate name field logic`() {
        assertNull(viewModel.uiState.value.nameErrorResId)

        viewModel.onNameChange("")
        assertEquals(
            R.string.error_registration_invalid_name,
            viewModel.uiState.value.nameErrorResId
        )

        viewModel.onNameChange("Marcelo Souza")
        assertNull(viewModel.uiState.value.nameErrorResId)
    }

    @Test
    fun `validate password complexity rules`() {
        viewModel.onPasswordChange("123")
        assertEquals(
            R.string.error_registration_weak_password,
            viewModel.uiState.value.passwordErrorResId
        )

        viewModel.onPasswordChange("123456")
        assertEquals(
            R.string.error_password_needs_special_char,
            viewModel.uiState.value.passwordErrorResId
        )

        viewModel.onPasswordChange("12345@")
        assertNull(viewModel.uiState.value.passwordErrorResId)
    }

    @Test
    fun `signUp should handle loading state and success navigation`() = runTest {
        coEvery { authRepository.register(any(), any(), any()) } coAnswers {
            delay(100)
            Result.success(Unit)
        }

        viewModel.onNameChange("Marcelo")
        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456@")

        viewModel.navigationEvent.test {

            viewModel.signUp()

            assertTrue("Deveria estar em loading", viewModel.uiState.value.isLoading)

            val navEvent = awaitItem()
            assertEquals(NavigationEvent.NavigateAndClear(HomeKey), navEvent)

            assertFalse("Deveria ter parado o loading", viewModel.uiState.value.isLoading)
        }
    }

    @Test
    fun `signUp failure should update errorResId in uiState`() = runTest {
        val exception = Exception("Network Error")
        coEvery { authRepository.register(any(), any(), any()) } returns Result.failure(exception)

        viewModel.onNameChange("Marcelo")
        viewModel.onEmailChange("marcelo@souza.com")
        viewModel.onPasswordChange("123456@")

        viewModel.signUp()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorResId)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onTogglePasswordVisibility should change state`() {
        val initialVisibility = viewModel.uiState.value.isPasswordVisible

        viewModel.onTogglePasswordVisibility()
        assertNotEquals(initialVisibility, viewModel.uiState.value.isPasswordVisible)

        viewModel.onTogglePasswordVisibility()
        assertEquals(initialVisibility, viewModel.uiState.value.isPasswordVisible)
    }
}