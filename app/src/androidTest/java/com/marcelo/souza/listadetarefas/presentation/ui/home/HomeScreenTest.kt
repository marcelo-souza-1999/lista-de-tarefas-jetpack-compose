package com.marcelo.souza.listadetarefas.presentation.ui.home

import android.content.Context
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marcelo.souza.listadetarefas.R
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskPriority
import com.marcelo.souza.listadetarefas.presentation.navigation.AppNavigator
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.model.SignUpKey
import com.marcelo.souza.listadetarefas.presentation.theme.ListaDeTarefasTheme
import com.marcelo.souza.listadetarefas.presentation.viewmodel.HomeViewModel
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
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val mockNavigator = mockk<AppNavigator>(relaxed = true)
    private val mockViewModel = mockk<HomeViewModel>(relaxed = true)

    private val uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    private val selectedFilter = MutableStateFlow(TaskFilter.ALL)
    private val dialogError = MutableStateFlow<DataError?>(null)
    private val showLogoutDialog = MutableStateFlow(false)
    private val taskPendingDelete = MutableStateFlow<Task?>(null)
    private val userName = MutableStateFlow("Marcelo")
    private val navigationEvent = MutableSharedFlow<NavigationEvent>()

    @Before
    fun setup() {
        every { mockViewModel.uiState } returns uiState
        every { mockViewModel.selectedFilter } returns selectedFilter
        every { mockViewModel.dialogError } returns dialogError
        every { mockViewModel.showLogoutDialog } returns showLogoutDialog
        every { mockViewModel.taskPendingDelete } returns taskPendingDelete
        every { mockViewModel.userName } returns userName
        every { mockViewModel.navigationEvent } returns navigationEvent
    }

    private fun setContent() {
        composeTestRule.setContent {
            ListaDeTarefasTheme {
                HomeScreen(navigator = mockNavigator, viewModel = mockViewModel)
            }
        }
    }

    @Test
    fun whenStateIsLoadingDisplaysFabButton() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()

        composeTestRule.onNodeWithTag("add_task_fab_btn").performClick()

        verify { mockViewModel.onAddTaskClicked() }
    }

    @Test
    fun whenStateIsLoadingDisplaysCircularProgress() {
        uiState.value = HomeUiState.Loading

        setContent()

        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccessWithEmptyListDisplaysEmptyState() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()
        composeTestRule.onNodeWithText(context.getString(R.string.empty_tasks_title))
            .assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccessWithTasksDisplaysList() {
        val tasks = listOf(Task("1", "Task Test", "Desc", TaskPriority.HIGH, false))
        uiState.value = HomeUiState.Success(tasks)
        setContent()
        composeTestRule.onNodeWithText("Task Test").assertIsDisplayed()
    }

    // --- 2. TESTES DE FILTROS E CLIQUES ---

    @Test
    fun whenFilterIsClickedCallsViewModel() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.filter_completed)).performClick()
        verify { mockViewModel.onFilterChange(TaskFilter.COMPLETED) }
    }

    @Test
    fun whenFabIsClickedCallsViewModel() {
        setContent()
        composeTestRule.onNodeWithTag("add_task_fab_btn").performClick()
        verify { mockViewModel.onAddTaskClicked() }
    }

    @Test
    fun whenShowLogoutDialogIsTrueDisplaysDialog() {
        showLogoutDialog.value = true
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.logout_dialog_title))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Sair").performClick()
        verify { mockViewModel.logout() }
    }

    @Test
    fun whenTaskPendingDeleteIsNotNullDisplaysDeleteDialog() {
        val task = Task("1", "Deletar", "Desc", TaskPriority.LOW, false)
        taskPendingDelete.value = task
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.delete_task_dialog_title))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Excluir").performClick()
        verify { mockViewModel.confirmDeleteTask() }
    }

    @Test
    fun whenUnknownErrorOccursDisplaysErrorGetTasksMessage() {
        dialogError.value = DataError.Unknown()

        setContent()

        val expectedMessage = context.getString(R.string.message_error_dialog_get_tasks)

        composeTestRule
            .onAllNodesWithText(expectedMessage)
            .fetchSemanticsNodes().isNotEmpty()

        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()

        composeTestRule.onNodeWithText("Tentar novamente").performClick()
        verify { mockViewModel.dismissDialog(HomeViewModel.HomeDialogType.ERROR) }
    }

    @Test
    fun whenNavigationEventIsNavigateCallsNavigator() = runTest {
        setContent()
        val route = SignUpKey

        navigationEvent.emit(NavigationEvent.Navigate(route))

        composeTestRule.waitForIdle()
        verify { mockNavigator.navigate(route) }
    }

    @Test
    fun whenLogoutIconIsClickedCallsViewModel() {
        setContent()
        composeTestRule.onNodeWithTag("logout_btn").performClick()
        verify { mockViewModel.onLogoutClick() }
    }

    @Test
    fun whenTaskIsClickedCallsEditTaskViewModel() {
        val task = Task("1", "Editar esta", "Desc", TaskPriority.HIGH, false)
        uiState.value = HomeUiState.Success(listOf(task))
        setContent()

        composeTestRule.onNodeWithTag("edit_task_btn").performClick()

        verify { mockViewModel.onEditTask(task) }
    }

    @Test
    fun whenDeleteIconIsClickedCallsViewModelRequestDelete() {
        val task = Task(
            id = "1",
            title = "Deletar esta",
            description = "Desc",
            priority = TaskPriority.HIGH,
            isCompleted = false
        )
        uiState.value = HomeUiState.Success(listOf(task))
        setContent()

        composeTestRule.onNodeWithTag("delete_task_btn").performClick()

        verify { mockViewModel.requestDeleteTask(task) }
    }

    @Test
    fun whenNavigationEventIsNavigateBackCallsNavigator() = runTest {
        setContent()

        navigationEvent.emit(NavigationEvent.NavigateBack)

        composeTestRule.waitForIdle()
        verify { mockNavigator.navigateBack() }
    }

    @Test
    fun whenFilterAllIsClickedCallsViewModelWithAll() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.filter_all)).performClick()

        verify { mockViewModel.onFilterChange(TaskFilter.ALL) }
    }

    @Test
    fun whenFilterPendingIsClickedCallsViewModelWithPending() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.filter_pending)).performClick()

        verify { mockViewModel.onFilterChange(TaskFilter.PENDING) }
    }

    @Test
    fun whenFilterCompletedIsClickedCallsViewModelWithCompleted() {
        uiState.value = HomeUiState.Success(emptyList())
        setContent()

        composeTestRule.onNodeWithText(context.getString(R.string.filter_completed)).performClick()

        verify { mockViewModel.onFilterChange(TaskFilter.COMPLETED) }
    }
}