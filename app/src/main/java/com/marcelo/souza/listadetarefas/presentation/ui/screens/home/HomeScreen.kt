package com.marcelo.souza.listadetarefas.presentation.ui.screens.home

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.HomeUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskFilter
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.EmptyTasksState
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskLazyColumn
import com.marcelo.souza.listadetarefas.presentation.ui.components.TopBar
import com.marcelo.souza.listadetarefas.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToCreateTask: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is HomeUiState.Error) {
            snackbarHostState.showSnackbar(message = context.getString(state.error.toMessageRes()))
        }
    }

    val tasks = (uiState as? HomeUiState.Success)?.tasks.orEmpty()
    val filteredTasks = tasks.filterBy(selectedFilter)

    HomeScreenContent(
        tasks = filteredTasks,
        isLoading = uiState is HomeUiState.Loading,
        selectedFilter = selectedFilter,
        onFilterChange = viewModel::onFilterChange,
        onAddTaskClick = onNavigateToCreateTask,
        onTaskCheckedChange = viewModel::onTaskCheckedChange,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun HomeScreenContent(
    tasks: List<TaskViewData>,
    isLoading: Boolean,
    selectedFilter: TaskFilter,
    onFilterChange: (TaskFilter) -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskCheckedChange: (TaskViewData, Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { AnimatedAddFab(onClick = onAddTaskClick) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopBar(
                modifier = Modifier.padding(horizontal = dimens.size0),
                userName = "Marcelo Souza",
                onLogoutClick = {}
            )

            Spacer(modifier = Modifier.height(dimens.size8))

            TaskFilterRow(
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange,
                modifier = Modifier.padding(horizontal = dimens.size16)
            )

            Spacer(modifier = Modifier.height(dimens.size8))

            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                tasks.isEmpty() -> {
                    EmptyTasksState(
                        title = stringResource(R.string.empty_tasks_title),
                        description = stringResource(R.string.empty_tasks_description)
                    )
                }

                else -> {
                    TaskLazyColumn(
                        tasks = tasks,
                        onTaskCheckedChange = onTaskCheckedChange,
                        onTaskClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskFilterRow(
    selectedFilter: TaskFilter,
    onFilterChange: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimens.size8)
    ) {
        TaskFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterChange(filter) },
                label = {
                    Text(
                        text = stringResource(
                            when (filter) {
                                TaskFilter.ALL -> R.string.filter_all
                                TaskFilter.PENDING -> R.string.filter_pending
                                TaskFilter.COMPLETED -> R.string.filter_completed
                            }
                        )
                    )
                }
            )
        }
    }
}

private fun List<TaskViewData>.filterBy(filter: TaskFilter): List<TaskViewData> {
    return when (filter) {
        TaskFilter.ALL -> this
        TaskFilter.PENDING -> filter { !it.isCompleted }
        TaskFilter.COMPLETED -> filter { it.isCompleted }
    }
}

private fun DataError.toMessageRes(): Int = when (this) {
    is DataError.Network -> R.string.message_error_dialog_registration_task_network
    is DataError.Permission -> R.string.message_error_dialog_registration_task_permission
    is DataError.Unknown -> R.string.message_error_dialog_registration_task_unknown
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
        contentColor = MaterialTheme.colorScheme.onPrimary
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
                TaskViewData("1", "Organizar Workspace", "Limpar a mesa e organizar os cabos do setup.", "HIGH", false),
                TaskViewData("2", "Academia", "Treino de pernas e 30 minutos de cardio.", "LOW", true)
            ),
            isLoading = false,
            selectedFilter = TaskFilter.ALL,
            onFilterChange = {},
            onAddTaskClick = {},
            onTaskCheckedChange = { _, _ -> },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview(name = "Home Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        HomeScreenContent(
            tasks = emptyList(),
            isLoading = false,
            selectedFilter = TaskFilter.ALL,
            onFilterChange = {},
            onAddTaskClick = {},
            onTaskCheckedChange = { _, _ -> },
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
