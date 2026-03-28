package com.marcelo.souza.listadetarefas.presentation.snapshot.splash

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.splash.SplashScreenContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenSnapshotTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun splashScreen_snapshot_light() {
        runSplashSnapshot(darkTheme = false, name = "splash_screen_light")
    }

    @Test
    fun splashScreen_snapshot_dark() {
        runSplashSnapshot(darkTheme = true, name = "splash_screen_dark")
    }

    private fun runSplashSnapshot(darkTheme: Boolean, name: String) {
        composeRule.setContent {
            ListaDeTarefasTheme(darkTheme = darkTheme) {
                SplashScreenContent(alpha = 1f)
            }
        }
        composeRule.waitForIdle()
        compareScreenshot(composeRule, name)
    }
}