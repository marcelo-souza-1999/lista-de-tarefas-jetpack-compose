package com.marcelo.souza.listadetarefas.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.data.utils.Constants.USERS_COLLECTION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

data class RegistrationUiFormState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorResId: Int? = null
)

@KoinViewModel
class RegistrationViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiFormState())
    val uiState = _uiState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        if (_uiState.value.isLoading) return

        val trimmedName = name.trim()
        val trimmedEmail = email.trim()

        if (trimmedName.isBlank()) {
            _uiState.update { it.copy(errorResId = R.string.error_registration_invalid_name) }
            return
        }

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
                firebaseAuth.createUserWithEmailAndPassword(trimmedEmail, password).await()
                firebaseAuth.currentUser?.updateProfile(
                    userProfileChangeRequest {
                        displayName = trimmedName
                    }
                )?.await()

                val uid = firebaseAuth.currentUser?.uid.orEmpty()
                if (uid.isNotBlank()) {
                    firestore.collection(USERS_COLLECTION)
                        .document(uid)
                        .set(
                            mapOf(
                                "name" to trimmedName,
                                "email" to trimmedEmail
                            )
                        )
                        .await()
                }
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorResId = throwable.toRegistrationErrorResId()
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

    private fun Throwable.toRegistrationErrorResId(): Int {
        return when (this) {
            is FirebaseAuthWeakPasswordException -> R.string.error_registration_weak_password
            is FirebaseAuthUserCollisionException -> R.string.error_registration_email_in_use
            is FirebaseNetworkException -> R.string.error_common_network
            is FirebaseAuthException -> R.string.error_common_unknown
            else -> R.string.error_common_unknown
        }
    }
}
