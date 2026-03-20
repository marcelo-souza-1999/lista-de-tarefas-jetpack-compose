package com.marcelo.souza.listadetarefas.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthException
import com.marcelo.souza.listadetarefas.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorResId: Int? = null
)

@KoinViewModel
class LoginViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value.isLoading) return

        val trimmedEmail = email.trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _uiState.update { it.copy(errorResId = R.string.error_login_invalid_credentials) }
            return
        }

        if (password.length < 6) {
            _uiState.update { it.copy(errorResId = R.string.error_registration_weak_password) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null, isSuccess = false) }

            runCatching {
                firebaseAuth.signInWithEmailAndPassword(trimmedEmail, password).await()
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorResId = throwable.toLoginErrorResId()
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorResId = null) }
    }

    fun consumeSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    private fun Throwable.toLoginErrorResId(): Int {
        return when (this) {
            is FirebaseAuthInvalidCredentialsException -> R.string.error_login_invalid_credentials
            is FirebaseAuthInvalidUserException -> R.string.error_login_user_not_found
            is FirebaseNetworkException -> R.string.error_common_network
            is FirebaseAuthException -> R.string.error_common_unknown
            else -> R.string.error_common_unknown
        }
    }
}
