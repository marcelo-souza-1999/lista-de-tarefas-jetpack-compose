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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.RegistrationUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskPriorityEnum
import com.marcelo.souza.listadetarefas.domain.model.UiEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.PrimaryButton
import com.marcelo.souza.listadetarefas.presentation.ui.components.SecondaryTopBar
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskErrorFancyDialog
import com.marcelo.souza.listadetarefas.presentation.ui.components.TaskSuccessFancyDialog
import com.marcelo.souza.listadetarefas.presentation.utils.toColor
import com.marcelo.souza.listadetarefas.presentation.viewmodel.RegistrationTaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationTaskScreen(
    onBackClick: () -> Unit,
    viewModel: RegistrationTaskViewModel = koinViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isButtonEnabled by remember(viewModel.title, uiState) {
        derivedStateOf {
            viewModel.title.trim().isNotBlank() && uiState !is RegistrationUiState.Loading
        }
    }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorToDisplay by remember { mutableStateOf<DataError?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.HideKeyboard -> keyboardController?.hide()
            }
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is RegistrationUiState.Success -> showSuccessDialog = true
            is RegistrationUiState.Error -> errorToDisplay = state.error
            else -> Unit
        }
    }

    if (showSuccessDialog) {
        TaskSuccessFancyDialog(
            title = stringResource(R.string.title_success_dialog_registration_task),
            message = stringResource(R.string.message_success_dialog_registration_task),
            onConfirmClick = onBackClick,
            onDismissRequest = onBackClick
        )
    }

    errorToDisplay?.let { error ->
        TaskErrorFancyDialog(
            title = stringResource(R.string.title_error_dialog_registration_task),
            message = stringResource(error.toMessageRes()),
            onRetryClick = {
                errorToDisplay = null
                viewModel.clearErrorState()
                viewModel.saveTask()
            },
            onCancelClick = {
                errorToDisplay = null
                viewModel.clearErrorState()
            },
            onDismissRequest = {
                errorToDisplay = null
                viewModel.clearErrorState()
            }
        )
    }

    RegistrationTaskContent(
        title = viewModel.title,
        onTitleChange = viewModel::onTitleChange,
        description = viewModel.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        selectedTaskPriorityEnum = viewModel.selectedPriority,
        onPrioritySelect = viewModel::onPriorityChange,
        onSaveClick = viewModel::saveTask,
        onBackClick = onBackClick,
        isLoading = uiState is RegistrationUiState.Loading,
        isSaveButtonEnabled = isButtonEnabled
    )
}

private fun DataError.toMessageRes(): Int = when (this) {
    is DataError.Network -> R.string.message_error_dialog_registration_task_network
    is DataError.Permission -> R.string.message_error_dialog_registration_task_permission
    is DataError.Unknown -> R.string.message_error_dialog_registration_task_unknown
}

@Composable
private fun RegistrationTaskContent(
    title: String,
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
    val priorities = remember { TaskPriorityEnum.entries }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.statusBarsPadding()
            ) {
                SecondaryTopBar(
                    title = stringResource(R.string.title_topbar_registration_tasks),
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.size12)
            ) {
                priorities.forEach { priority ->
                    val isSelected = selectedTaskPriorityEnum == priority
                    val priorityColor = priority.toColor()

                    FilterChip(
                        selected = isSelected,
                        onClick = { onPrioritySelect(priority) },
                        label = {
                            Text(
                                text = stringResource(id = priority.labelResId),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else priorityColor
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = priorityColor,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = priorityColor,
                            selectedBorderColor = priorityColor,
                            borderWidth = dimens.size1
                        )
                    )
                }
            }
        }
    }
}

@Preview(name = "Add Task Light", showBackground = true)
@Composable
private fun RegistrationTaskScreenPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        RegistrationTaskContent(
            title = "",
            onTitleChange = {},
            description = "",
            onDescriptionChange = {},
            selectedTaskPriorityEnum = TaskPriorityEnum.LOW,
            onPrioritySelect = {},
            onSaveClick = {},
            onBackClick = {},
            isSaveButtonEnabled = false,
            isLoading = false
        )
    }
}

@Preview(name = "Add Task Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegistrationTaskScreenDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        RegistrationTaskContent(
            title = "Aprender ViewModel",
            onTitleChange = {},
            description = "Estudar os conceitos de StateFlow e UiState",
            onDescriptionChange = {},
            selectedTaskPriorityEnum = TaskPriorityEnum.HIGH,
            onPrioritySelect = {},
            onSaveClick = {},
            onBackClick = {},
            isSaveButtonEnabled = true,
            isLoading = true
        )
    }
}
