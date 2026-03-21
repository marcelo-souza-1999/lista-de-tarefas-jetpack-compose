package com.marcelo.souza.listadetarefas.presentation.ui.splash

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.theme.LocalDimens
import com.marcelo.souza.listadetarefas.presentation.viewmodel.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    var startAnimation by remember { mutableStateOf(true) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateAndClear -> {
                    if (event.route == LoginKey) {
                        onNavigateToLogin()
                    } else if (event.route == HomeKey) {
                        onNavigateToHome()
                    }
                }

                else -> Unit
            }
        }
    }

    SplashScreenContent(alpha = alphaAnim)
}

@Composable
fun SplashScreenContent(alpha: Float) {
    val dimens = LocalDimens.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .graphicsLayer(alpha = alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(dimens.size120),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(dimens.size24))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(
    name = "Splash Screen Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun SplashScreenPreview() {
    ListaDeTarefasTheme(darkTheme = true) {
        Surface {
            SplashScreenContent(
                alpha = 1f
            )
        }
    }
}