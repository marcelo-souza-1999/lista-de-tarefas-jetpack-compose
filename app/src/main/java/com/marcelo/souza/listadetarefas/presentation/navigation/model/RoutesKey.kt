package com.marcelo.souza.listadetarefas.presentation.navigation.model

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface RoutesKey : NavKey

@Serializable
data object SplashKey : RoutesKey

@Serializable
data object HomeKey : RoutesKey

@Serializable
data object LoginKey : RoutesKey

@Serializable
data object SignUpKey : RoutesKey

@Serializable
data class TaskFormKey(
    val task: TaskNavigationModel? = null
) : RoutesKey