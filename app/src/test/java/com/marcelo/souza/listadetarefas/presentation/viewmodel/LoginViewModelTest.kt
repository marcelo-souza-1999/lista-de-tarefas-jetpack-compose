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

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mockk<AuthenticateRepository>()

    @Test
    fun `onEmailChange should invalidate invalid email`() {
        val viewModel = LoginViewModel(authRepository)

        viewModel.onEmailChange("invalid")

        assertTrue(viewModel.uiState.value.emailErrorResId != null)
        assertTrue(!viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `login should navigate on success`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(Unit)
        val viewModel = LoginViewModel(authRepository)
        viewModel.onEmailChange("user@mail.com")
        viewModel.onPasswordChange("123456")

        viewModel.login()

        assertEquals(NavigationEvent.NavigateAndClear(HomeKey), viewModel.navigationEvent.first())
    }
}
