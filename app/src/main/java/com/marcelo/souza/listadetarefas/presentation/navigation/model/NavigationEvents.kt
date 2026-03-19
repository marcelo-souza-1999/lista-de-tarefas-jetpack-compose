package com.marcelo.souza.listadetarefas.presentation.navigation.model

sealed class NavigationEvent {
    data class Navigate(val route: RoutesKey) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}