package com.marcelo.souza.listadetarefas.presentation.viewmodel

import com.marcelo.souza.listadetarefas.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = mockk<AuthenticateRepository>()

    @Test
    fun `should navigate to home when user logged in`() = runTest {
        every { authRepository.isUserLoggedIn() } returns true

        val viewModel = SplashViewModel(authRepository)
        advanceTimeBy(1500)

        assertEquals(NavigationEvent.NavigateAndClear(HomeKey), viewModel.navigationEvent.first())
    }

    @Test
    fun `should navigate to login when user is not logged in`() = runTest {
        every { authRepository.isUserLoggedIn() } returns false

        val viewModel = SplashViewModel(authRepository)
        advanceTimeBy(1500)

        assertEquals(NavigationEvent.NavigateAndClear(LoginKey), viewModel.navigationEvent.first())
    }
}
