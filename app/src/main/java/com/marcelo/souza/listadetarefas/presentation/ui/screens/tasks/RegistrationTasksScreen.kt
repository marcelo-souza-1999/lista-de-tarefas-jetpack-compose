package com.marcelo.souza.listadetarefas.presentation.ui.screens.tasks

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.TaskPriorityEnum
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.model.UiEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.PrimaryButton
import com.marcelo.souza.listadetarefas.presentation.ui.components.SecondaryTopBar
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskErrorFancyDialog
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskSuccessFancyDialog
import com.marcelo.souza.listadetarefas.presentation.utils.toColor
import com.marcelo.souza.listadetarefas.presentation.utils.toMessageRes
import com.marcelo.souza.listadetarefas.presentation.viewmodel.RegistrationTaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationTaskScreen(
    navigator: AppNavigator,
    task: TaskViewData? = null,
    viewModel: RegistrationTaskViewModel = koinViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isButtonEnabled = uiState.title.isNotBlank() && !uiState.isLoading

    LaunchedEffect(task) {
        task?.let {
            viewModel.loadTask(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.HideKeyboard -> keyboardController?.hide()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> navigator.navigate(event.route)
                NavigationEvent.NavigateBack -> navigator.navigateBack()
            }
        }
    }

    if (uiState.showSuccessDialog) {
        val title = if (uiState.isEditing)
            stringResource(R.string.title_success_dialog_edit_task)
        else
            stringResource(R.string.title_success_dialog_registration_task)

        val message = if (uiState.isEditing)
            stringResource(R.string.message_success_dialog_edit_task)
        else
            stringResource(R.string.message_success_dialog_registration_task)

        TaskSuccessFancyDialog(
            title = title,
            message = message,
            isCancelable = false,
            onConfirmClick = {
                viewModel.resetForm()
                viewModel.onSuccessConfirmed()
            },
            onDismissRequest = {
                viewModel.dismissSuccessDialog()
            }
        )
    }

    uiState.error?.let { error ->
        TaskErrorFancyDialog(
            title = stringResource(R.string.title_error_dialog_registration_task),
            message = stringResource(error.toMessageRes()),
            onRetryClick = {
                viewModel.dismissError()
                viewModel.saveTask()
            },
            onCancelClick = viewModel::dismissError,
            onDismissRequest = viewModel::dismissError
        )
    }

    val titleTopBar = if (uiState.isEditing)
        stringResource(R.string.title_topbar_registration_edit_tasks)
    else
        stringResource(R.string.title_topbar_registration_tasks)

    RegistrationTaskContent(
        title = uiState.title,
        titleTopBar = titleTopBar,
        onTitleChange = viewModel::onTitleChange,
        description = uiState.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        selectedTaskPriorityEnum = uiState.selectedPriority,
        onPrioritySelect = viewModel::onPriorityChange,
        onSaveClick = viewModel::saveTask,
        onBackClick = viewModel::onBackClicked,
        isLoading = uiState.isLoading,
        isSaveButtonEnabled = isButtonEnabled
    )
}

@Composable
private fun RegistrationTaskContent(
    title: String,
    titleTopBar: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedTaskPriorityEnum: TaskPriorityEnum,
    onPrioritySelect: (TaskPriorityEnum) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    isSaveButtonEnabled: Boolean
) {
    val dimens = LocalDimens.current
    val priorities = TaskPriorityEnum.entries

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.statusBarsPadding()
            ) {
                SecondaryTopBar(
                    title = titleTopBar,
                    onBackClick = onBackClick
                )
            }
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.navigationBarsPadding()
            ) {
                PrimaryButton(
                    text = stringResource(R.string.register_tasks_button_text),
                    onClick = onSaveClick,
                    isLoading = isLoading,
                    enabled = isSaveButtonEnabled,
                    modifier = Modifier.padding(dimens.size24)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.size24),
            verticalArrangement = Arrangement.spacedBy(dimens.size16)
        ) {

            Spacer(modifier = Modifier.height(dimens.size8))

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.title_register_task_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text(stringResource(R.string.description_register_task_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.size120),
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(dimens.size8))

            Text(
                text = stringResource(R.string.title_priority_register_task_label),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.size12)
            ) {
                priorities.forEach { priority ->
                    val isSelected = selectedTaskPriorityEnum == priority
                    val color = priority.toColor()

                    FilterChip(
                        selected = isSelected,
                        onClick = { onPrioritySelect(priority) },
                        label = {
                            Text(
                                text = stringResource(priority.labelResId),
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimary
                                else color
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLight() {
    ListaDeTarefasTheme {
        RegistrationTaskContent(
            title = "Nova Task",
            titleTopBar = stringResource(R.string.title_topbar_registration_edit_tasks),
            onTitleChange = {},
            description = "Descrição",
            onDescriptionChange = {},
            selectedTaskPriorityEnum = TaskPriorityEnum.HIGH,
            onPrioritySelect = {},
            onSaveClick = {},
            onBackClick = {},
            isLoading = false,
            isSaveButtonEnabled = true
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    ListaDeTarefasTheme(darkTheme = true) {
        RegistrationTaskContent(
            title = "",
            titleTopBar = stringResource(R.string.title_topbar_registration_tasks),
            onTitleChange = {},
            description = "",
            onDescriptionChange = {},
            selectedTaskPriorityEnum = TaskPriorityEnum.LOW,
            onPrioritySelect = {},
            onSaveClick = {},
            onBackClick = {},
            isLoading = false,
            isSaveButtonEnabled = false
        )
    }
}