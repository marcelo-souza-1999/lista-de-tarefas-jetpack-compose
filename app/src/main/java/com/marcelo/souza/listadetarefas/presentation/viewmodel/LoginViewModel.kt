package com.marcelo.souza.listadetarefas.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.LoginUiState
import com.marcelo.souza.listadetarefas.presentation.ui.authenticate.toLoginErrorResId
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val authRepository: AuthenticateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEmailChange(newValue: String) {
        val errorId =
            if (newValue.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(newValue).matches()) {
                R.string.error_email_invalid_credentials
            } else null

        updateState { it.copy(email = newValue, emailErrorResId = errorId) }
    }

    fun onPasswordChange(newValue: String) {
        val errorId = if (newValue.isNotEmpty() && newValue.length < MIN_PASSWORD_LENGTH) {
            R.string.error_registration_weak_password
        } else null

        updateState { it.copy(password = newValue, passwordErrorResId = errorId) }
    }

    private fun updateState(transform: (LoginUiState) -> LoginUiState) {
        _uiState.update { currentState ->
            val updated = transform(currentState)
            updated.copy(
                isFormValid = updated.email.isNotBlank() &&
                        updated.emailErrorResId == null &&
                        updated.password.isNotBlank() &&
                        updated.passwordErrorResId == null
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        val state = _uiState.value

        if (state.isLoading) return

        if (!state.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }

            authRepository.login(state.email.trim(), state.password)
                .fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.send(NavigationEvent.NavigateAndClear(HomeKey))
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorResId = throwable.toLoginErrorResId()
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