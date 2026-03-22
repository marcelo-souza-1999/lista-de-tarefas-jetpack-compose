package com.marcelo.souza.listadetarefas.presentation.utils

import androidx.compose.ui.graphics.Color
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityHigh
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityLow
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityMedium

fun TaskPriority.toColor(): Color {
    return when (this) {
        TaskPriority.LOW -> PriorityLow
        TaskPriority.MEDIUM -> PriorityMedium
        TaskPriority.HIGH -> PriorityHigh
    }
}

fun TaskPriority.toLabelResId(): Int {
    return when (this) {
        TaskPriority.LOW -> R.string.task_priority_low
        TaskPriority.MEDIUM -> R.string.task_priority_medium
        TaskPriority.HIGH -> R.string.task_priority_high
    }
}