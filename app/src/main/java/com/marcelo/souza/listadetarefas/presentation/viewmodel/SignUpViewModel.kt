package com.marcelo.souza.listadetarefas.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.SignUpUiState
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.toSignUpErrorResId
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignUpViewModel(
    private val authRepository: AuthenticateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onNameChange(newName: String) {
        val errorId = if (newName.isEmpty()) {
            R.string.error_registration_invalid_name
        } else null

        updateState { it.copy(name = newName, nameErrorResId = errorId) }
    }

    fun onEmailChange(newEmail: String) {
        val errorId =
            if (newEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                R.string.error_email_invalid_credentials
            } else null

        updateState { it.copy(email = newEmail, emailErrorResId = errorId) }
    }

    fun onPasswordChange(newPassword: String) {
        val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }
        val errorId = when {
            newPassword.isEmpty() -> null
            newPassword.length < MIN_PASSWORD_LENGTH -> R.string.error_registration_weak_password
            !hasSpecialChar -> R.string.error_password_needs_special_char
            else -> null
        }
        updateState { it.copy(password = newPassword, passwordErrorResId = errorId) }
    }

    private fun updateState(transform: (SignUpUiState) -> SignUpUiState) {
        _uiState.update { currentState ->
            val updated = transform(currentState)
            updated.copy(
                isFormValid = updated.name.isNotBlank() &&
                        updated.nameErrorResId == null &&
                        updated.email.isNotBlank() &&
                        updated.emailErrorResId == null &&
                        updated.password.length >= MIN_PASSWORD_LENGTH &&
                        updated.passwordErrorResId == null
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun signUp() {
        val state = _uiState.value
        if (state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }
            authRepository.register(state.name.trim(), state.email.trim(), state.password)
                .fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.send(NavigationEvent.NavigateAndClear(HomeKey))
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorResId = throwable.toSignUpErrorResId()
                            )
                        }
                    }
                )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorResId = null) }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}