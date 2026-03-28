package com.marcelo.souza.listadetarefas.presentation.ui.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.viewmodel.SplashViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<SplashViewModel>(relaxed = true)
    private val navigationEvent = MutableSharedFlow<NavigationEvent>()

    private val onNavigateToLogin: () -> Unit = mockk(relaxed = true)
    private val onNavigateToHome: () -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        every { mockViewModel.navigationEvent } returns navigationEvent
    }

    private fun setContent() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                SplashScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToHome = onNavigateToHome,
                    viewModel = mockViewModel
                )
            }
        }
    }

    @Test
    fun whenScreenIsDisplayedShowsAppName() {
        setContent()

        composeTestRule.onNodeWithText("Lista de Tarefas", substring = true).assertIsDisplayed()
    }

    @Test
    fun whenEventIsNavigateToLoginCallsCallback() = runTest {
        setContent()

        navigationEvent.emit(NavigationEvent.NavigateAndClear(LoginKey))

        composeTestRule.waitForIdle()

        verify { onNavigateToLogin() }
    }

    @Test
    fun whenEventIsNavigateToHomeCallsCallback() = runTest {
        setContent()

        navigationEvent.emit(NavigationEvent.NavigateAndClear(HomeKey))

        composeTestRule.waitForIdle()

        verify { onNavigateToHome() }
    }

    @Test
    fun whenEventIsOtherNavigationDoesNothing() = runTest {
        setContent()

        navigationEvent.emit(NavigationEvent.NavigateBack)

        composeTestRule.waitForIdle()

        verify(exactly = 0) { onNavigateToLogin() }
        verify(exactly = 0) { onNavigateToHome() }
    }
}