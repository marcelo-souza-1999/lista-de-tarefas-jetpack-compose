package com.marcelo.souza.listadetarefas.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class AppColors(
    val border: Color,
    val textSecondary: Color,
    val inputBackground: Color,
    val taskCardBackground: Color,
    val navBarBackground: Color
)

private val LocalAppColors = staticCompositionLocalOf { DefaultLightAppColors }

private val DefaultLightAppColors = AppColors(
    border = BorderGray,
    textSecondary = TextSecondaryLight,
    inputBackground = Color.White,
    taskCardBackground = Color.White,
    navBarBackground = Color.White
)

private val DefaultDarkAppColors = AppColors(
    border = BorderGrayDark,
    textSecondary = TextSecondaryDark,
    inputBackground = Color(0xFF1E293B),
    taskCardBackground = Color(0xFF1E293B),
    navBarBackground = Color(0xFF0F172A)
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    secondary = IndigoSecondary,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = IndigoPrimaryDark,
    secondary = IndigoSecondary,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.Black,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    error = ErrorRed
)

@Immutable
data class Transparency(
    val low: Float = 0.2f,
    val disabled: Float = 0.5f
)

val LocalAppTransparency = staticCompositionLocalOf { Transparency() }

@Composable
fun ListaDeTarefasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DefaultDarkAppColors else DefaultLightAppColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val colors: AppColors @Composable get() = LocalAppColors.current
    val transparency: Transparency @Composable get() = LocalAppTransparency.current
}