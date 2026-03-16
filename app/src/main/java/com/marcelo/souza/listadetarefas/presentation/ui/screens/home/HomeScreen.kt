package com.marcelo.souza.listadetarefas.presentation.ui.screens.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.LazyColumn
import com.marcelo.souza.listadetarefas.presentation.ui.components.TopBar

// TODO: Substituir futuramente pelo seu TaskViewData que vem da camada de domínio
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: String
)

@Composable
fun HomeScreen(
    onNavigateToAddActivity: () -> Unit,
    // viewModel: HomeViewModel = koinViewModel() // <-- Futuramente
) {
    val context = LocalContext.current

    // TODO: Isso virá de um StateFlow do ViewModel (ex: viewModel.uiState.collectAsStateWithLifecycle())
    val tasks = remember {
        mutableStateListOf(
            Task(1, "Organizar Workspace", "Limpar a mesa e organizar os cabos do setup.", "Alta"),
            Task(2, "Finalizar curso de Compose", "Assistir as últimas aulas.", "Média"),
            Task(3, "Academia", "Treino de pernas e 30 minutos de cardio.", "Baixa")
        )
    }

    HomeScreenContent(
        tasks = tasks,
        userName = "Marcelo Souza", // Futuramente virá do Auth/ViewModel
        onAddTaskClick = onNavigateToAddActivity,
        onLogoutClick = {
            // Lógica de logout do ViewModel
            Toast.makeText(context, "Saindo...", Toast.LENGTH_SHORT).show()
        },
        onDeleteTask = { taskToDelete ->
            // viewModel.deleteTask(taskToDelete.id)
            tasks.remove(taskToDelete)
        },
        onEditTask = { taskToEdit ->
            // onNavigateToEditActivity(taskToEdit.id)
            Toast.makeText(context, "Editando: ${taskToEdit.title}", Toast.LENGTH_SHORT).show()
        }
    )
}

@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    userName: String,
    onAddTaskClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AnimatedAddFab(onClick = onAddTaskClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopBar(
                modifier = Modifier.padding(horizontal = dimens.size0),
                userName = userName,
                onLogoutClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(dimens.size8))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                tasks = tasks,
                onDeleteTask = onDeleteTask,
                onEditTask = onEditTask
            )
        }
    }
}

@Composable
private fun AnimatedAddFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "fab_scale_animation"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 90f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "fab_rotation_animation"
    )

    FloatingActionButton(
        modifier = modifier.scale(scale),
        onClick = onClick,
        interactionSource = interactionSource,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = MaterialTheme.shapes.large
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.graphicsLayer(rotationZ = rotation)
        )
    }
}

@Preview(name = "Home Light", showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        HomeScreenContent(
            tasks = listOf(
                Task(1, "Preview Task", "Descrição da preview", "Alta")
            ),
            userName = "Marcelo Souza",
            onAddTaskClick = {},
            onLogoutClick = {},
            onDeleteTask = {},
            onEditTask = {}
        )
    }
}

@Preview(name = "Home Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreviewDark() {
    ListaDeTarefasTheme(darkTheme = true) {
        HomeScreenContent(
            tasks = emptyList(),
            userName = "Marcelo",
            onAddTaskClick = {},
            onLogoutClick = {},
            onDeleteTask = {},
            onEditTask = {}
        )
    }
}