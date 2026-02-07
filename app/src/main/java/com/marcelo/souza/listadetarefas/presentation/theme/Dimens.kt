package com.marcelo.souza.listadetarefas.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimens(
    val size0: Dp = 0.dp,
    val size2: Dp = 2.dp,
    val size4: Dp = 4.dp,
    val size8: Dp = 8.dp,
    val size16: Dp = 16.dp,
    val size24: Dp = 24.dp,
    val size32: Dp = 32.dp,
    val size38: Dp = 38.dp,
    val size48: Dp = 48.dp,
    val size100: Dp = 100.dp
)

val LocalDimens = staticCompositionLocalOf { Dimens() }