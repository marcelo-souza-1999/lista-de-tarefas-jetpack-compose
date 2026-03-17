package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens

@Composable
fun TaskLazyColumn(
    tasks: List<TaskViewData>,
    onTaskCheckedChange: (TaskViewData, Boolean) -> Unit,
    onTaskClick: (TaskViewData) -> Unit,
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
            key = { it.id.ifBlank { it.title } }
        ) { task ->
            TaskCard(
                task = task,
                onTaskClick = onTaskClick,
                onCheckedChange = { isChecked -> onTaskCheckedChange(task, isChecked) }
            )
        }
    }
}

@Preview(name = "List Light Mode", showBackground = true)
@Composable
internal fun TaskListLightPreview() {
    val fakePreviewTasks = listOf(
        TaskViewData("1", "Tarefa Urgente", "Esta é uma tarefa de alta prioridade para testar o vermelho.", "HIGH", false),
        TaskViewData("2", "Tarefa Comum", "Uma tarefa normal do dia a dia, cor amarela.", "MEDIUM", false),
        TaskViewData("3", "Relaxar", "Tarefa tranquila, cor verde.", "LOW", true)
    )
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaskLazyColumn(
                tasks = fakePreviewTasks,
                onTaskCheckedChange = { _, _ -> },
                onTaskClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "List Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TaskListDarkPreview() {
    val fakePreviewTasks = listOf(
        TaskViewData("1", "Tarefa Urgente", "Esta é uma tarefa de alta prioridade para testar o vermelho.", "HIGH", false),
        TaskViewData("2", "Tarefa Comum", "Uma tarefa normal do dia a dia, cor amarela.", "MEDIUM", false),
        TaskViewData("3", "Relaxar", "Tarefa tranquila, cor verde.", "LOW", true)
    )
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            TaskLazyColumn(
                tasks = fakePreviewTasks,
                onTaskCheckedChange = { _, _ -> },
                onTaskClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
