package com.marcelo.souza.listadetarefas.presentation.navigation.model

import androidx.navigation3.runtime.NavKey
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import kotlinx.serialization.Serializable

@Serializable
sealed interface RoutesKey : NavKey

@Serializable
data object HomeKey : RoutesKey

@Serializable
data class RegistrationTaskKey(
    val task: TaskViewData? = null
) : RoutesKey