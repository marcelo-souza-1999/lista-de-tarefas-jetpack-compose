package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.presentation.theme.AppTheme
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens

@Composable
fun InputTextField(
    text: String,
    onValueText: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val dimens = LocalDimens.current

    OutlinedTextField(
        value = text,
        onValueChange = onValueText,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        keyboardOptions = keyboardOptions,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.size16),
        leadingIcon = leadingIcon,
        trailingIcon = if (isError && trailingIcon == null) {
            {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        } else {
            trailingIcon
        },
        visualTransformation = visualTransformation,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = AppTheme.colors.border,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = AppTheme.colors.textSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,

            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error
        )
    )

    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = dimens.size16, top = dimens.size4)
        )
    }
}

@Preview(
    name = "Dark Mode Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun InputTextFieldPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        val dimens = LocalDimens.current

        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(true) }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(dimens.size16)
        ) {
            Column(modifier = Modifier.padding(dimens.size16)) {
                InputTextField(
                    text = emailText,
                    onValueText = { emailText = it },
                    label = "E-mail",
                    placeholder = "Insira seu e-mail",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary
                        )
                    }
                )

                Spacer(modifier = Modifier.height(dimens.size16))

                InputTextField(
                    text = passwordText,
                    onValueText = { passwordText = it },
                    label = "Senha",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary
                        )
                    },
                    trailingIcon = {
                        val image = if (isPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }

                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Light Mode Preview",
    showBackground = true
)
@Composable
internal fun InputTextFieldLightPreview() {
    ListaDeTarefasTheme(darkTheme = false) {
        val dimens = LocalDimens.current

        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(true) }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(dimens.size16)
        ) {
            Column(modifier = Modifier.padding(dimens.size16)) {
                InputTextField(
                    text = emailText,
                    onValueText = { emailText = it },
                    label = "E-mail",
                    placeholder = "Insira seu e-mail",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary
                        )
                    }
                )

                Spacer(modifier = Modifier.height(dimens.size16))

                InputTextField(
                    text = passwordText,
                    onValueText = { passwordText = it },
                    label = "Senha",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = AppTheme.colors.textSecondary
                        )
                    },
                    trailingIcon = {
                        val image = if (isPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }

                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}