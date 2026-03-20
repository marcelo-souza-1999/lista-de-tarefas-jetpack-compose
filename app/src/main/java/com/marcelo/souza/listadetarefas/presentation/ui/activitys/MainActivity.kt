package com.marcelo.souza.listadetarefas.presentation.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.screens.home.HomeScreen
import com.marcelo.souza.listadetarefas.presentation.ui.screens.login.LoginScreen
import com.marcelo.souza.listadetarefas.presentation.ui.screens.login.RegistrationScreen
import com.marcelo.souza.listadetarefas.presentation.ui.screens.tasks.RegistrationTaskScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeTarefasTheme {
                val initialScreen = if (FirebaseAuth.getInstance().currentUser != null) {
                    Screen.Home
                } else {
                    Screen.Login
                }

                val backStack = remember { mutableStateListOf(initialScreen) }
                val currentScreen = backStack.last()

                when (currentScreen) {
                    Screen.Login -> LoginScreen(
                        onNavigateToRegister = { backStack.add(Screen.Registration) },
                        onLoginSuccess = {
                            backStack.clear()
                            backStack.add(Screen.Home)
                        }
                    )

                    Screen.Registration -> RegistrationScreen(
                        onNavigateToLogin = { backStack.removeLastOrNull() },
                        onRegisterSuccess = {
                            backStack.clear()
                            backStack.add(Screen.Home)
                        }
                    )

                    Screen.Home -> HomeScreen(
                        onNavigateToCreateTask = { backStack.add(Screen.RegistrationTask) },
                        onLoggedOut = {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    )

                    Screen.RegistrationTask -> RegistrationTaskScreen(
                        onBackClick = {
                            if (backStack.size > 1) {
                                backStack.removeLast()
                            } else {
                                finish()
                            }
                        }
                    )
                }
            }
        }
    }
}

private enum class Screen {
    Login,
    Registration,
    Home,
    RegistrationTask
}
