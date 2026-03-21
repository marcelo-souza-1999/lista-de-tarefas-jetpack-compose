package com.marcelo.souza.listadetarefas.presentation.ui.authenticate

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.AppTheme
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.ui.components.InputTextField
import com.marcelo.souza.listadetarefas.presentation.ui.components.PrimaryButton
import com.marcelo.souza.listadetarefas.presentation.ui.components.SecondaryButton
import com.marcelo.souza.listadetarefas.presentation.ui.components.dialogs.TaskErrorFancyDialog
import com.marcelo.souza.listadetarefas.presentation.viewmodel.SignUpViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dimens = LocalDimens.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateAndClear -> onSignUpSuccess()
                else -> Unit
            }
        }
    }

    uiState.errorResId?.let { errorResId ->
        TaskErrorFancyDialog(
            title = stringResource(R.string.title_error_dialog_auth),
            message = stringResource(errorResId),
            onRetryClick = {
                viewModel.clearError()
                viewModel.signUp()
            },
            onCancelClick = viewModel::clearError,
            onDismissRequest = viewModel::clearError
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(horizontal = dimens.size24)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimens.size32))

            Icon(
                imageVector = Icons.Default.AppRegistration,
                contentDescription = null,
                modifier = Modifier.size(dimens.size100),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimens.size24))

            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center
                )
            )

            Text(
                text = stringResource(R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(top = dimens.size8)
            )

            Spacer(modifier = Modifier.height(dimens.size48))

            InputTextField(
                text = uiState.name,
                onValueText = viewModel::onNameChange,
                label = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.placeholder_name),
                isError = uiState.nameErrorResId != null,
                errorMessage = uiState.nameErrorResId?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(dimens.size16))

            InputTextField(
                text = uiState.email,
                onValueText = viewModel::onEmailChange,
                label = stringResource(id = R.string.label_email),
                isError = uiState.emailErrorResId != null,
                errorMessage = uiState.emailErrorResId?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(dimens.size16))

            InputTextField(
                text = uiState.password,
                onValueText = viewModel::onPasswordChange,
                label = stringResource(id = R.string.label_password),
                placeholder = stringResource(id = R.string.placeholder_password),
                isError = uiState.passwordErrorResId != null,
                errorMessage = uiState.passwordErrorResId?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { viewModel.signUp() }),
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(dimens.size32))

            PrimaryButton(
                text = stringResource(R.string.register_button_text),
                onClick = viewModel::signUp,
                isLoading = uiState.isLoading,
                enabled = uiState.isFormValid
            )

            Spacer(modifier = Modifier.height(dimens.size16))

            SecondaryButton(
                text = stringResource(R.string.login_link_text),
                onClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(dimens.size24))
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun SignUpScreenPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            SignUpScreen(
                onNavigateToLogin = {},
                onSignUpSuccess = {}
            )
        }
    }
}