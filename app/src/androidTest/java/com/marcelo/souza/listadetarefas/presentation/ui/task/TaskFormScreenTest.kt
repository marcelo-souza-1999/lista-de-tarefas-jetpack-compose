package com.marcelo.souza.listadetarefas.presentation.ui.task

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.ui.home.UiEvent
import com.marcelo.souza.listadetarefas.presentation.utils.toLabelResId
import com.marcelo.souza.listadetarefas.presentation.viewmodel.TaskFormViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val mockNavigator = mockk<AppNavigator>(relaxed = true)
    private val mockViewModel = mockk<TaskFormViewModel>(relaxed = true)
    
    private val uiState = MutableStateFlow(TaskFormUiState())
    private val uiEvent = MutableSharedFlow<UiEvent>()
    private val navigationEvent = MutableSharedFlow<NavigationEvent>()

    @Before
    fun setup() {
        every { mockViewModel.uiState } returns uiState
        every { mockViewModel.uiEvent } returns uiEvent
        every { mockViewModel.navigationEvent } returns navigationEvent
    }

    private fun setContent(task: Task? = null) {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                TaskFormScreen(
                    navigator = mockNavigator,
                    task = task,
                    viewModel = mockViewModel
                )
            }
        }
    }

    @Test
    fun whenTaskIsProvidedCallsLoadTask() {
        val task = Task("1", "Editar", "Desc", TaskPriority.HIGH, false)
        setContent(task = task)
        
        verify { mockViewModel.loadTask(task) }
    }

    @Test
    fun whenStateIsCreateModeDisplaysCorrectTitlesAndButton() {
        uiState.value = TaskFormUiState(isEditing = false)
        setContent()

        val expectedTopBarTitle = context.getString(R.string.title_topbar_registration_tasks)
        val expectedButtonTitle = context.getString(R.string.register_tasks_button_text)

        composeTestRule.onNodeWithText(expectedTopBarTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedButtonTitle).assertIsDisplayed()
    }

    @Test
    fun whenStateIsEditModeDisplaysCorrectTitlesAndButton() {
        uiState.value = TaskFormUiState(isEditing = true)
        setContent()

        val expectedTopBarTitle = context.getString(R.string.title_topbar_registration_edit_tasks)
        val expectedButtonTitle = context.getString(R.string.edit_tasks_button_text)

        composeTestRule.onNodeWithText(expectedTopBarTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedButtonTitle).assertIsDisplayed()
    }

    @Test
    fun whenTitleIsBlankSaveButtonIsDisabled() {
        uiState.value = TaskFormUiState(title = "", isLoading = false, isEditing = false)
        setContent()

        val buttonTitle = context.getString(R.string.register_tasks_button_text)
        composeTestRule.onNodeWithText(buttonTitle).assertIsNotEnabled()
    }

    @Test
    fun whenTitleIsFilledSaveButtonIsEnabled() {
        uiState.value = TaskFormUiState(title = "Estudar Testes", isLoading = false, isEditing = false)
        setContent()

        val buttonTitle = context.getString(R.string.register_tasks_button_text)
        composeTestRule.onNodeWithText(buttonTitle).assertIsEnabled()
    }

    @Test
    fun whenTitleIsTypedCallsViewModel() {
        setContent()

        val titleLabel = context.getString(R.string.title_register_task_label)
        composeTestRule.onNodeWithText(titleLabel).performTextInput("Nova Task")

        verify { mockViewModel.onTitleChange("Nova Task") }
    }

    @Test
    fun whenDescriptionIsTypedCallsViewModel() {
        setContent()

        val descLabel = context.getString(R.string.description_register_task_label)
        composeTestRule.onNodeWithText(descLabel).performTextInput("Fazer testes 100%")

        verify { mockViewModel.onDescriptionChange("Fazer testes 100%") }
    }

    @Test
    fun whenPriorityIsClickedCallsViewModel() {
        setContent()

        val highPriorityText = context.getString(TaskPriority.HIGH.toLabelResId())
        composeTestRule.onNodeWithText(highPriorityText).performClick()

        verify { mockViewModel.onPriorityChange(TaskPriority.HIGH) }
    }

    @Test
    fun whenSaveButtonIsClickedCallsViewModel() {
        uiState.value = TaskFormUiState(title = "Task Válida", isEditing = false)
        setContent()

        val buttonTitle = context.getString(R.string.register_tasks_button_text)
        composeTestRule.onNodeWithText(buttonTitle).performClick()

        verify { mockViewModel.saveTask() }
    }

    @Test
    fun whenBackButtonClickedCallsViewModel() {
        setContent()

        composeTestRule.onNodeWithTag("back_btn").performClick()

        verify { mockViewModel.onBackClicked() }
    }

    @Test
    fun whenShowSuccessDialogIsTrueInCreateModeDisplaysCreateMessage() {
        uiState.value = TaskFormUiState(showSuccessDialog = true, isEditing = false)
        setContent()

        val expectedMessage = context.getString(R.string.message_success_dialog_registration_task)

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText(expectedMessage).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()

        composeTestRule.onNodeWithText("OK").performClick()

        verify { mockViewModel.resetForm() }
        verify { mockViewModel.onSuccessConfirmed() }
    }

    @Test
    fun whenShowSuccessDialogIsTrueInEditModeDisplaysEditMessage() {
        uiState.value = TaskFormUiState(showSuccessDialog = true, isEditing = true)
        setContent()

        val expectedMessage = context.getString(R.string.message_success_dialog_edit_task)

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText(expectedMessage).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

    @Test
    fun whenErrorOccursDisplaysErrorDialogAndRetryCallsViewModel() {
        uiState.value = TaskFormUiState(error = DataError.Unknown())
        setContent()

        val expectedTitle = context.getString(R.string.title_error_dialog_registration_task)

        composeTestRule.waitUntil(5000) {
            composeTestRule.onAllNodesWithText(expectedTitle).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Tentar novamente").performClick()

        verify { mockViewModel.dismissError() }
        verify { mockViewModel.saveTask() }
    }

    @Test
    fun whenNavigationEventIsNavigateBackCallsNavigator() = runTest {
        setContent()

        navigationEvent.emit(NavigationEvent.NavigateBack)

        composeTestRule.waitForIdle()
        verify { mockNavigator.navigateBack() }
    }

    @Test
    fun whenUiEventIsHideKeyboardTriggersEffect() = runTest {
        setContent()
        
        uiEvent.emit(UiEvent.HideKeyboard)
        composeTestRule.waitForIdle()
    }

    @Test
    fun whenNavigationEventIsNavigateCallsNavigator() = runTest {
        setContent()

        val rotaDestiny = HomeKey

        navigationEvent.emit(NavigationEvent.Navigate(rotaDestiny))

        composeTestRule.waitForIdle()

        verify { mockNavigator.navigate(rotaDestiny) }
    }
}