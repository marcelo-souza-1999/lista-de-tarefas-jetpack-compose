package com.marcelo.souza.listadetarefas.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.SignUpKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.SplashKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.TaskFormKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.toDomainModel
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginScreen
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpScreen
import com.marcelo.souza.listadetarefas.presentation.ui.home.HomeScreen
import com.marcelo.souza.listadetarefas.presentation.ui.splash.SplashScreen
import com.marcelo.souza.listadetarefas.presentation.ui.task.TaskFormScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeTarefasTheme {
                StartNavigation(onFinish = { finish() })
            }
        }
    }
}

private const val NAVIGATION_ANIMATION_DURATION = 700

@Composable
fun StartNavigation(onFinish: () -> Unit) {
    val startDestination = SplashKey

    val backStack = rememberNavBackStack(startDestination)
    val navigator = remember { AppNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
            ) togetherWith fadeOut(
                animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
            )
        },
        entryProvider = entryProvider {
            entry<SplashKey> {
                SplashScreen(
                    onNavigateToLogin = {
                        navigator.navigateAndClear(LoginKey)
                    },
                    onNavigateToHome = {
                        navigator.navigateAndClear(HomeKey)
                    }
                )
            }

            entry<LoginKey> {
                LoginScreen(
                    onNavigateToRegister = {
                        navigator.navigate(SignUpKey)
                    },
                    onLoginSuccess = {
                        navigator.navigateAndClear(HomeKey)
                    }
                )
            }

            entry<SignUpKey> {
                SignUpScreen(
                    onNavigateToLogin = {
                        navigator.navigateBack()
                    },
                    onSignUpSuccess = {
                        navigator.navigateAndClear(HomeKey)
                    }
                )
            }

            entry<HomeKey> {
                HomeScreen(
                    navigator = navigator
                )
            }

            entry<TaskFormKey> { key ->
                TaskFormScreen(
                    navigator = navigator,
                    task = key.task?.toDomainModel()
                )
            }
        },
        onBack = {
            if (backStack.size > 1) {
                navigator.navigateBack()
            } else {
                onFinish()
            }
        }
    )
}