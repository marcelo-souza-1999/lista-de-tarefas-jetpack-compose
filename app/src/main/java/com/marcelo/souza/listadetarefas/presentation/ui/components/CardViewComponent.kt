package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.presentation.theme.AppTheme
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityHigh
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityLow
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityMedium

@Composable
fun TaskCard(
    task: TaskViewData,
    onCheckedChange: (Boolean) -> Unit,
    onTaskClick: (TaskViewData) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    val priorityColor = when (task.priority) {
        "HIGH" -> PriorityHigh
        "MEDIUM" -> PriorityMedium
        else -> PriorityLow
    }

    val priorityLabel = when (task.priority) {
        "HIGH" -> stringResource(R.string.task_priority_high)
        "MEDIUM" -> stringResource(R.string.task_priority_medium)
        else -> stringResource(R.string.task_priority_low)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(dimens.size24),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.size0)
    ) {
        Column(modifier = Modifier.padding(dimens.size20)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckedChange
                )

                Spacer(modifier = Modifier.width(dimens.size8))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }

            Spacer(modifier = Modifier.height(dimens.size8))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            Spacer(modifier = Modifier.height(dimens.size16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(dimens.size12)
                            .clip(CircleShape)
                            .background(priorityColor)
                    )
                    Spacer(modifier = Modifier.width(dimens.size8))
                    Text(
                        text = priorityLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.textSecondary
                    )
                }

                Text(
                    text = if (task.isCompleted) stringResource(R.string.task_completed) else stringResource(R.string.task_pending),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
internal fun TaskCardLightPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(24.dp)) {
            TaskCard(
                task = TaskViewData(
                    id = "1",
                    title = "Fazer Café",
                    description = "Comprar grãos novos e passar um café fresquinho.",
                    priority = "LOW",
                    isCompleted = false
                ),
                onTaskClick = {},
                onCheckedChange = {}
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TaskCardDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(24.dp)) {
            TaskCard(
                task = TaskViewData(
                    id = "1",
                    title = "Refatorar Cores",
                    description = "Agora as cores estão centralizadas no arquivo Color.kt como solicitado.",
                    priority = "HIGH",
                    isCompleted = true
                ),
                onTaskClick = {},
                onCheckedChange = {}
            )
        }
    }
}
