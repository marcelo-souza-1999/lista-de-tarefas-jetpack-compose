package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_HIGH
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_LOW
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_MEDIUM
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.presentation.theme.EditBlue
import com.marcelo.souza.listadetarefas.presentation.theme.ErrorRed
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens

@Composable
fun TaskLazyColumn(
    tasks: List<TaskViewData>,
    onTaskCheckedChange: (TaskViewData, Boolean) -> Unit,
    onEditTask: (TaskViewData) -> Unit,
    onDeleteTask: (TaskViewData) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current
    val haptic = LocalHapticFeedback.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = dimens.size16,
            end = dimens.size16,
            top = dimens.size8,
            bottom = dimens.size100
        ),
        verticalArrangement = Arrangement.spacedBy(dimens.size16)
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            val dismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(dismissState.currentValue) {
                when (dismissState.currentValue) {

                    SwipeToDismissBoxValue.StartToEnd -> {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDeleteTask(task)

                        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                    }

                    SwipeToDismissBoxValue.EndToStart -> {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEditTask(task)

                        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                    }

                    SwipeToDismissBoxValue.Settled -> Unit
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            SwipeToDismissBoxValue.StartToEnd -> ErrorRed
                            SwipeToDismissBoxValue.EndToStart -> EditBlue
                            else -> Color.Transparent
                        },
                        label = "DismissColorAnimation"
                    )

                    val isDeleting =
                        dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd
                    val alignment = if (isDeleting) Alignment.CenterStart else Alignment.CenterEnd
                    val icon = if (isDeleting) Icons.Default.Delete else Icons.Default.Edit

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color, shape = RoundedCornerShape(dimens.size16))
                            .padding(horizontal = dimens.size24),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                content = {
                    TaskCard(
                        task = task,
                        onCheckedChange = { isChecked -> onTaskCheckedChange(task, isChecked) },
                        onEditClick = { onEditTask(task) },
                        onDeleteClick = { onDeleteTask(task) }
                    )
                }
            )
        }
    }
}

@Preview(name = "List Light Mode", showBackground = true)
@Composable
internal fun TaskListLightPreview() {
    val fakePreviewTasks = getFakeTasks()
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaskLazyColumn(
                tasks = fakePreviewTasks,
                onTaskCheckedChange = { _, _ -> },
                onDeleteTask = {},
                onEditTask = {}
            )
        }
    }
}

@Preview(name = "List Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TaskListDarkPreview() {
    val fakePreviewTasks = getFakeTasks()
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaskLazyColumn(
                tasks = fakePreviewTasks,
                onTaskCheckedChange = { _, _ -> },
                onDeleteTask = {},
                onEditTask = {}
            )
        }
    }
}

private fun getFakeTasks() = listOf(
    TaskViewData("1", "Tarefa Urgente", "Prioridade alta.", PRIORITY_HIGH, false),
    TaskViewData("2", "Tarefa Média", "Prioridade média.", PRIORITY_MEDIUM, false),
    TaskViewData("3", "Relaxar", "Prioridade baixa.", PRIORITY_LOW, true)
)