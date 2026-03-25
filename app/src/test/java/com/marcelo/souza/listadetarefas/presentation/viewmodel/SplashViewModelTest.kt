package com.marcelo.souza.listadetarefas.presentation.viewmodel

import app.cash.turbine.test
import com.marcelo.souza.listadetarefas.utils.MainDispatcherRule
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

        viewModel.navigationEvent.test {
            advanceTimeBy(1501)

            val event = awaitItem()
            assertEquals(NavigationEvent.NavigateAndClear(HomeKey), event)
        }
    }

    @Test
    fun `should navigate to login when user is not logged in`() = runTest {
        every { authRepository.isUserLoggedIn() } returns false

        val viewModel = SplashViewModel(authRepository)

        viewModel.navigationEvent.test {
            advanceTimeBy(1501)

            val event = awaitItem()
            assertEquals(NavigationEvent.NavigateAndClear(LoginKey), event)
        }
    }

    @Test
    fun `should NOT navigate before splash delay`() = runTest {
        every { authRepository.isUserLoggedIn() } returns true
        val viewModel = SplashViewModel(authRepository)

        viewModel.navigationEvent.test {
            advanceTimeBy(1000)
            expectNoEvents()
        }
    }

    @Test
    fun `should navigate to login when repository throws exception`() = runTest {
        coEvery { authRepository.isUserLoggedIn() } throws Exception("Database error")

        val viewModel = SplashViewModel(authRepository)

        viewModel.navigationEvent.test {
            advanceTimeBy(1501)
            val event = awaitItem()
            assertEquals(NavigationEvent.NavigateAndClear(LoginKey), event)
        }
    }
}
