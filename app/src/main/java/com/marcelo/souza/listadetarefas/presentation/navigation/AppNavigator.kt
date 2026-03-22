package com.marcelo.souza.listadetarefas.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.RoutesKey

class AppNavigator(
    private val backStack: NavBackStack<NavKey>
) {
    fun navigate(route: RoutesKey) {
        if (backStack.lastOrNull() != route) {
            backStack.add(route)
        }
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    fun navigateAndClear(route: RoutesKey) {
        backStack.clear()
        backStack.add(route)
    }
}