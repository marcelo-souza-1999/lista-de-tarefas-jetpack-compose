package com.marcelo.souza.listadetarefas.presentation.ui.authenticate

import com.marcelo.souza.listadetarefas.R
import java.net.UnknownHostException

data class LoginUiState(
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorResId: Int? = null,
    val isFormValid: Boolean = false
)

data class SignUpUiState(
    val name: String = "",
    val nameErrorResId: Int? = null,
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isLoading: Boolean = false,
    val errorResId: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false
)

fun Throwable.toLoginErrorResId(): Int {
    val message = this.message.orEmpty()
    return when {
        message.contains("auth credential is incorrect") ||
                message.contains("INVALID_LOGIN_CREDENTIALS") -> R.string.error_login_invalid_credentials

        message.contains("USER_NOT_FOUND") -> R.string.error_login_user_not_found
        this is UnknownHostException -> R.string.error_common_network
        else -> R.string.error_common_unknown
    }
}

fun Throwable.toSignUpErrorResId(): Int {
    val message = this.message.orEmpty()
    return when {
        message.contains("PASSWORD_DOES_NOT_MEET_REQUIREMENTS") -> R.string.error_registration_weak_password
        message.contains("EMAIL_EXISTS") -> R.string.error_registration_email_in_use
        message.contains("PERMISSION_DENIED") -> R.string.message_error_dialog_permission
        this is UnknownHostException -> R.string.error_common_network
        else -> R.string.error_common_unknown
    }
}