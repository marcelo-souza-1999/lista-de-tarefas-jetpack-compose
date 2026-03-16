package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcelo.souza.listadetarefas.presentation.theme.EditBlue
import com.marcelo.souza.listadetarefas.presentation.theme.ErrorRed
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.screens.home.Task

@Composable
fun LazyColumn(
    tasks: List<Task>,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

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
            val dismissState = rememberSwipeToDismissBoxState(
                initialValue = SwipeToDismissBoxValue.Settled
            )

            LaunchedEffect(dismissState.currentValue) {
                when (dismissState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> {
                        onDeleteTask(task)
                    }

                    SwipeToDismissBoxValue.EndToStart -> {
                        onEditTask(task)
                        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                    }

                    SwipeToDismissBoxValue.Settled -> {}
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val direction = dismissState.dismissDirection
                    val targetValue = dismissState.targetValue

                    val color by animateColorAsState(
                        when (targetValue) {
                            SwipeToDismissBoxValue.StartToEnd -> ErrorRed
                            SwipeToDismissBoxValue.EndToStart -> EditBlue
                            else -> Color.Transparent
                        }, label = "ColorAnimation"
                    )

                    val scale by animateFloatAsState(
                        if (targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                        label = "ScaleAnimation"
                    )

                    val alignment = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }

                    val icon = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Delete
                        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Edit
                        else -> Icons.Default.Delete
                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                color,
                                shape = RoundedCornerShape(dimens.size24)
                            )
                            .padding(horizontal = dimens.size24),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.scale(scale),
                            tint = Color.White
                        )
                    }
                },
                content = {
                    CardView(
                        title = task.title,
                        description = task.description,
                        priority = task.priority,
                        onDeleteClick = { onDeleteTask(task) },
                        onEditClick = { onEditTask(task) }
                    )
                }
            )
        }
    }
}

@Preview(
    name = "List Light Mode",
    showBackground = true
)
@Composable
internal fun TaskListLightPreview() {
    val fakePreviewTasks = listOf(
        Task(
            1,
            "Tarefa Urgente",
            "Esta é uma tarefa de alta prioridade para testar o vermelho.",
            "Alta"
        ),
        Task(2, "Tarefa Comum", "Uma tarefa normal do dia a dia, cor amarela.", "Média"),
        Task(3, "Relaxar", "Tarefa tranquila, cor verde.", "Baixa"),
        Task(4, "Outra Tarefa", "Mais um item para encher a lista e testar o scroll.", "Baixa")
    )
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                tasks = fakePreviewTasks,
                onDeleteTask = { _: Task -> /* Lógica de delete */ },
                onEditTask = { _: Task -> /* Lógica de edit */ },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(
    name = "List Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun TaskListDarkPreview() {
    val fakePreviewTasks = listOf(
        Task(
            1,
            "Tarefa Urgente",
            "Esta é uma tarefa de alta prioridade para testar o vermelho.",
            "Alta"
        ),
        Task(2, "Tarefa Comum", "Uma tarefa normal do dia a dia, cor amarela.", "Média"),
        Task(3, "Relaxar", "Tarefa tranquila, cor verde.", "Baixa"),
        Task(4, "Outra Tarefa", "Mais um item para encher a lista e testar o scroll.", "Baixa"),
        Task(5, "Outra Tarefa", "Mais um item para encher a lista e testar o scroll.", "Baixa"),
        Task(6, "Outra Tarefa", "Mais um item para encher a lista e testar o scroll.", "Baixa")
    )
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                tasks = fakePreviewTasks,
                onDeleteTask = { _: Task -> /* Lógica de delete */ },
                onEditTask = { _: Task -> /* Lógica de edit */ },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}