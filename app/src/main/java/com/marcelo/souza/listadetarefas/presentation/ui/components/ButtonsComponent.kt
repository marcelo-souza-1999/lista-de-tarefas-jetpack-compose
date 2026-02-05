package com.marcelo.souza.listadetarefas.presentation.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcelo.souza.listadetarefas.presentation.theme.AppTheme
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val dimens = LocalDimens.current
    val transparency = AppTheme.transparency

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size48),
        shape = RoundedCornerShape(dimens.size16),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = transparency.disabled),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = transparency.disabled)
        ),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimens.size24),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = dimens.size2
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = LocalDimens.current
    val transparency = AppTheme.transparency

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size38),
        shape = RoundedCornerShape(dimens.size16),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = transparency.low),
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = dimens.size0),
        contentPadding = PaddingValues(dimens.size0)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
internal fun ButtonsPreviewLight() {
    ListaDeTarefasTheme(darkTheme = false) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(text = "Entrar", onClick = {})
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Entrar", onClick = {}, isLoading = true)
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(text = "Não tem conta? Cadastre-se agora", onClick = {})
            }
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun ButtonsPreviewDark() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(text = "Entrar", onClick = {})
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Entrar", onClick = {}, isLoading = true)
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(text = "Não tem conta? Cadastre-se agora", onClick = {})
            }
        }
    }
}