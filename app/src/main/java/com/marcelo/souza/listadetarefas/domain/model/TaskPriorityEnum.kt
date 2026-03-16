package com.marcelo.souza.listadetarefas.domain.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityHigh
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityLow
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityMedium

enum class TaskPriorityEnum(
    @StringRes val labelResId: Int,
    val color: Color
) {
    LOW(R.string.task_priority_low, PriorityLow),
    MEDIUM(R.string.task_priority_medium, PriorityMedium),
    HIGH(R.string.task_priority_high, PriorityHigh)
}