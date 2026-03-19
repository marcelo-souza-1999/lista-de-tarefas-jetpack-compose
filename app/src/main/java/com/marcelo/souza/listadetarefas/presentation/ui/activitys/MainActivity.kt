package com.marcelo.souza.listadetarefas.presentation.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.RegistrationTaskKey
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.screens.home.HomeScreen
import com.marcelo.souza.listadetarefas.presentation.ui.screens.tasks.RegistrationTaskScreen

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

@Composable
fun StartNavigation(onFinish: () -> Unit) {
    val backStack = rememberNavBackStack(HomeKey)
    val navigator = remember { AppNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HomeKey> {
                HomeScreen(
                    navigator = navigator
                )
            }

            entry<RegistrationTaskKey> { key ->
                RegistrationTaskScreen(
                    navigator = navigator,
                    task = key.task
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