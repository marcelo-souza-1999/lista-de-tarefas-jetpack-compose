package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.Task
import com.marcelo.souza.listadetarefas.domain.model.TaskResult
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.model.TaskFormKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.toNavigationModel
import com.marcelo.souza.listadetarefas.presentation.ui.home.HomeUiState
import com.marcelo.souza.listadetarefas.presentation.ui.home.TaskFilter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val authRepository: AuthenticateRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _dialogError = MutableStateFlow<DataError?>(null)
    val dialogError = _dialogError.asStateFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val _taskPendingDelete = MutableStateFlow<Task?>(null)
    val taskPendingDelete = _taskPendingDelete.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    init {
        fetchUserDisplayName()
    }

    private fun fetchUserDisplayName() {
        viewModelScope.launch {
            val name = authRepository.fetchUserName()
            _userName.value = name
        }
    }

    private val _tasksFlow = taskRepository.getTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = TaskResult.Success(emptyList())
        )

    val uiState = combine(_tasksFlow, _selectedFilter) { result, filter ->
        when (result) {
            is TaskResult.Error -> HomeUiState.Error(result.error)
            is TaskResult.Success -> {
                val filteredList = when (filter) {
                    TaskFilter.ALL -> result.data
                    TaskFilter.PENDING -> result.data.filter { !it.isCompleted }
                    TaskFilter.COMPLETED -> result.data.filter { it.isCompleted }
                }
                HomeUiState.Success(filteredList)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = HomeUiState.Loading
    )

    fun onFilterChange(filter: TaskFilter) {
        _selectedFilter.value = filter
    }

    fun onTaskCheckedChange(task: Task, isChecked: Boolean) {
        viewModelScope.launch {
            val result = taskRepository.updateTaskCompletion(task.id, isChecked)
            if (result is TaskResult.Error) _dialogError.value = result.error
        }
    }

    fun onEditTask(task: Task) {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.Navigate(
                    TaskFormKey(task.toNavigationModel())
                )
            )
        }
    }

    fun requestDeleteTask(task: Task) {
        _taskPendingDelete.value = task
    }

    fun confirmDeleteTask() {
        val task = _taskPendingDelete.value ?: return

        viewModelScope.launch {
            val result = taskRepository.deleteTask(task.id)
            if (result is TaskResult.Error) {
                _dialogError.value = result.error
            }
            _taskPendingDelete.value = null
        }
    }

    fun dismissDialog(dialogType: HomeDialogType) {
        when (dialogType) {
            HomeDialogType.DELETE -> _taskPendingDelete.value = null
            HomeDialogType.ERROR -> _dialogError.value = null
            HomeDialogType.LOGOUT -> _showLogoutDialog.value = false
        }
    }

    fun onAddTaskClicked() {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.Navigate(route = TaskFormKey(null))
            )
        }
    }

    fun onLogoutClick() {
        _showLogoutDialog.value = true
    }

    fun logout() {
        viewModelScope.launch {
            _showLogoutDialog.value = false
            authRepository.logout()

            _navigationEvent.send(
                NavigationEvent.NavigateAndClear(LoginKey)
            )
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5000L
    }

    enum class HomeDialogType { DELETE, ERROR, LOGOUT }
}