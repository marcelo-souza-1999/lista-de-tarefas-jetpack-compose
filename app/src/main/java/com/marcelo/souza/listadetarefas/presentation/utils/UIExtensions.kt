package com.marcelo.souza.listadetarefas.presentation.utils

import androidx.compose.ui.graphics.Color
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.TaskPriorityEnum

fun TaskPriorityEnum.toColor(): Color = this.color

fun DataError.toMessageRes(): Int {
    return when (this) {
        is DataError.Network -> R.string.message_error_dialog_registration_task_network
        is DataError.Permission -> R.string.message_error_dialog_registration_task_permission
        is DataError.Unknown -> R.string.message_error_dialog_registration_task_unknown
    }
}