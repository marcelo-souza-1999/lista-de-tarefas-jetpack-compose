package com.marcelo.souza.listadetarefas.presentation.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.screens.home.HomeScreen
import com.marcelo.souza.listadetarefas.presentation.ui.screens.tasks.RegistrationTaskScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaDeTarefasTheme {
                val backStack = remember { mutableStateListOf(Screen.Home) }
                val currentScreen = backStack.last()

                when (currentScreen) {
                    Screen.Home -> HomeScreen(
                        onNavigateToCreateTask = {
                            backStack.add(Screen.RegistrationTask)
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
    Home,
    RegistrationTask
}
