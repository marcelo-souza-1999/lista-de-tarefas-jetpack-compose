package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.theme.AppTheme
import com.marcelo.souza.listadetarefas.presentation.theme.EditBlue
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityHigh
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityLow
import com.marcelo.souza.listadetarefas.presentation.theme.PriorityMedium

@Composable
fun CardView(
    title: String,
    description: String,
    priority: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    val priorityColor = when (priority) {
        stringResource(R.string.task_priority_high) -> PriorityHigh
        stringResource(R.string.task_priority_medium) -> PriorityMedium
        else -> PriorityLow
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.size24),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.size0)
    ) {
        Column(
            modifier = Modifier
                .padding(dimens.size20)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(dimens.size8))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier
                    .height(dimens.size20)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimens.size12)
                            .clip(CircleShape)
                            .background(priorityColor)
                    )
                    Spacer(modifier = Modifier.width(dimens.size8))
                    Text(
                        text = priority,
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.textSecondary
                    )
                }

                Row {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(dimens.size32)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = EditBlue,
                            modifier = Modifier.size(dimens.size20)
                        )
                    }

                    Spacer(modifier = Modifier.width(dimens.size8))

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(dimens.size32)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            modifier = Modifier.size(dimens.size20)
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun TaskItemCardDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(24.dp)
        ) {
            CardView(
                title = "Refatorar Cores",
                description = "Agora as cores estão centralizadas no arquivo Color.kt como solicitado.",
                priority = "Alta",
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
internal fun TaskItemCardLightPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(24.dp)
        ) {
            CardView(
                title = "Fazer Café",
                description = "Comprar grãos novos e passar um café fresquinho.",
                priority = "Baixa",
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}