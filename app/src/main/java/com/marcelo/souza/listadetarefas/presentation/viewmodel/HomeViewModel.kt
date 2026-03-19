package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.HomeUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskFilter
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import com.marcelo.souza.listadetarefas.presentation.navigation.model.RegistrationTaskKey
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
    private val repository: TaskRepository
) : ViewModel() {
    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _dialogError = MutableStateFlow<DataError?>(null)
    val dialogError = _dialogError.asStateFlow()

    private val _taskPendingDelete = MutableStateFlow<TaskViewData?>(null)
    val taskPendingDelete = _taskPendingDelete.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _tasksFlow = repository.getTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskResultViewData.Success(emptyList())
        )

    val uiState = combine(_tasksFlow, _selectedFilter) { result, filter ->
        when (result) {
            is TaskResultViewData.Error -> HomeUiState.Error(result.error)
            is TaskResultViewData.Success -> {
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun onFilterChange(filter: TaskFilter) {
        _selectedFilter.value = filter
    }

    fun onTaskCheckedChange(task: TaskViewData, isChecked: Boolean) {
        viewModelScope.launch {
            val result = repository.updateTaskCompletion(task.id, isChecked)
            if (result is TaskResultViewData.Error) _dialogError.value = result.error
        }
    }

    fun onEditTask(task: TaskViewData) {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.Navigate(
                    RegistrationTaskKey(task)
                )
            )
        }
    }

    fun requestDeleteTask(task: TaskViewData) {
        _taskPendingDelete.value = task
    }

    fun confirmDeleteTask() {
        val task = _taskPendingDelete.value ?: return

        viewModelScope.launch {
            val result = repository.deleteTask(task.id)
            if (result is TaskResultViewData.Error) {
                _dialogError.value = result.error
            }
            _taskPendingDelete.value = null
        }
    }

    fun dismissDeleteDialog() {
        _taskPendingDelete.value = null
    }

    fun dismissErrorDialog() {
        _dialogError.value = null
    }

    fun onAddTaskClicked() {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.Navigate(route = RegistrationTaskKey(null))
            )
        }
    }
}