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
import androidx.compose.material.icons.filled.CheckCircle
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
import com.marcelo.souza.listadetarefas.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateAndClear -> onLoginSuccess()
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
                viewModel.login()
            },
            onCancelClick = viewModel::clearError,
            onDismissRequest = viewModel::clearError
        )
    }

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onLoginClick = viewModel::login,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val dimens = LocalDimens.current
    val scrollState = rememberScrollState()

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
            Spacer(modifier = Modifier.height(dimens.size8))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(dimens.size100),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimens.size24))

            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(dimens.size48))

            InputTextField(
                text = uiState.email,
                onValueText = onEmailChange,
                isError = uiState.emailErrorResId != null,
                errorMessage = uiState.emailErrorResId?.let { stringResource(it) },
                label = stringResource(id = R.string.label_email),
                placeholder = stringResource(id = R.string.placeholder_email),
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
                onValueText = onPasswordChange,
                label = stringResource(id = R.string.label_password),
                placeholder = stringResource(id = R.string.placeholder_password),
                isError = uiState.passwordErrorResId != null,
                errorMessage = uiState.passwordErrorResId?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onLoginClick() }),
                visualTransformation = if (uiState.isPasswordVisible) {
                    VisualTransformation.None
                } else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
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
                text = stringResource(R.string.login_button_primary_text),
                onClick = onLoginClick,
                isLoading = uiState.isLoading,
                enabled = uiState.isFormValid
            )

            Spacer(modifier = Modifier.height(dimens.size16))

            SecondaryButton(
                text = stringResource(R.string.signup_button_secondary_text),
                onClick = onNavigateToRegister
            )

            Spacer(modifier = Modifier.height(dimens.size16))
        }
    }
}

@Preview(
    name = "Login Screen Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun LoginScreenDarkPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreenContent(
                uiState = LoginUiState(
                    email = "",
                    isFormValid = true
                ),
                onEmailChange = {},
                onPasswordChange = {},
                onTogglePasswordVisibility = {},
                onLoginClick = {},
                onNavigateToRegister = {}
            )
        }
    }
}