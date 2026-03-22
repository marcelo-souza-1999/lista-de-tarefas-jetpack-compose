package com.marcelo.souza.listadetarefas.presentation.viewmodel

import com.marcelo.souza.listadetarefas.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mockk<AuthenticateRepository>()

    @Test
    fun `onPasswordChange should require special character`() {
        val viewModel = SignUpViewModel(authRepository)

        viewModel.onPasswordChange("123456")

        assertTrue(viewModel.uiState.value.passwordErrorResId != null)
    }

    @Test
    fun `signUp should navigate on success`() = runTest {
        coEvery { authRepository.register(any(), any(), any()) } returns Result.success(Unit)
        val viewModel = SignUpViewModel(authRepository)

        viewModel.onNameChange("Marcelo")
        viewModel.onEmailChange("user@mail.com")
        viewModel.onPasswordChange("12345@")
        viewModel.signUp()

        assertEquals(NavigationEvent.NavigateAndClear(HomeKey), viewModel.navigationEvent.first())
    }
}
