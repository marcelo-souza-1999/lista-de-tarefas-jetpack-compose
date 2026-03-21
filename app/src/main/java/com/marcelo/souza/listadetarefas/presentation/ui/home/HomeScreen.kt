package com.marcelo.souza.listadetarefas.presentation.ui.home

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.EmptyTasksState
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskLazyColumn
import com.marcelo.souza.listadetarefas.presentation.ui.components.TopBar
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskDeleteConfirmationFancyDialog
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskErrorFancyDialog
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskLogoutConfirmationFancyDialog
import com.marcelo.souza.listadetarefas.presentation.utils.toMessageRes
import com.marcelo.souza.listadetarefas.presentation.viewmodel.HomeViewModel
import com.marcelo.souza.listadetarefas.presentation.viewmodel.HomeViewModel.HomeDialogType
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigator: AppNavigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val dialogError by viewModel.dialogError.collectAsStateWithLifecycle()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsStateWithLifecycle()
    val taskPendingDelete by viewModel.taskPendingDelete.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()

    val tasks = (uiState as? HomeUiState.Success)?.tasks.orEmpty()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> navigator.navigate(event.route)
                is NavigationEvent.NavigateAndClear -> navigator.navigateAndClear(event.route)
                NavigationEvent.NavigateBack -> navigator.navigateBack()
            }
        }
    }

    dialogError?.let { error ->
        TaskErrorFancyDialog(
            title = stringResource(R.string.title_error_dialog_registration_task),
            message = stringResource(error.toMessageRes()),
            onRetryClick = { viewModel.dismissDialog(HomeDialogType.ERROR) },
            onCancelClick = { viewModel.dismissDialog(HomeDialogType.ERROR) },
            onDismissRequest = { viewModel.dismissDialog(HomeDialogType.ERROR) }
        )
    }

    taskPendingDelete?.let { task ->
        TaskDeleteConfirmationFancyDialog(
            title = stringResource(R.string.delete_task_dialog_title),
            message = stringResource(R.string.delete_task_dialog_message, task.title),
            onConfirmDelete = viewModel::confirmDeleteTask,
            onCancelClick = { viewModel.dismissDialog(HomeDialogType.DELETE) },
            onDismissRequest = { viewModel.dismissDialog(HomeDialogType.DELETE) }
        )
    }

    if (showLogoutDialog) {
        TaskLogoutConfirmationFancyDialog(
            title = stringResource(R.string.logout_dialog_title),
            message = stringResource(R.string.logout_dialog_message),
            onConfirmLogout = viewModel::logout,
            onCancelClick = { viewModel.dismissDialog(HomeDialogType.LOGOUT) },
            onDismissRequest = { viewModel.dismissDialog(HomeDialogType.LOGOUT) }
        )
    }

    HomeScreenContent(
        tasks = tasks,
        isLoading = uiState is HomeUiState.Loading,
        selectedFilter = selectedFilter,
        userName = userName,
        onFilterChange = viewModel::onFilterChange,
        onAddTaskClick = viewModel::onAddTaskClicked,
        onTaskCheckedChange = viewModel::onTaskCheckedChange,
        onDeleteTask = viewModel::requestDeleteTask,
        onEditTask = viewModel::onEditTask,
        onLogoutClick = viewModel::onLogoutClick
    )
}

@Composable
private fun HomeScreenContent(
    tasks: List<Task>,
    isLoading: Boolean,
    selectedFilter: TaskFilter,
    userName: String,
    onFilterChange: (TaskFilter) -> Unit,
    onAddTaskClick: () -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = { AnimatedAddFab(onClick = onAddTaskClick) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopBar(
                userName = userName,
                onLogoutClick = onLogoutClick
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
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                        onDeleteTask = onDeleteTask,
                        onEditTask = onEditTask
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

@Composable
private fun AnimatedAddFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "fab_scale"
    )

    FloatingActionButton(
        modifier = modifier.scale(scale),
        onClick = onClick,
        interactionSource = interactionSource,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Task")
    }
}


@Preview(name = "Home Light Mode", showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenContent(
                tasks = getFakeTasks(),
                isLoading = false,
                userName = "Marcelo",
                selectedFilter = TaskFilter.COMPLETED,
                onFilterChange = {},
                onAddTaskClick = {},
                onTaskCheckedChange = { _, _ -> },
                onDeleteTask = {},
                onEditTask = {},
                onLogoutClick = {}
            )
        }
    }
}

@Preview(name = "Home Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreenContent(
                tasks = emptyList(),
                isLoading = false,
                userName = "Marcelo",
                selectedFilter = TaskFilter.ALL,
                onFilterChange = {},
                onAddTaskClick = {},
                onTaskCheckedChange = { _, _ -> },
                onDeleteTask = {},
                onEditTask = {},
                onLogoutClick = {}
            )
        }
    }
}

private fun getFakeTasks() = listOf(
    Task("1", "Task Reativa 1", "Lógica no ViewModel", TaskPriority.HIGH, false),
    Task("2", "Task Reativa 2", "UI focada em desenho", TaskPriority.LOW, true)
)