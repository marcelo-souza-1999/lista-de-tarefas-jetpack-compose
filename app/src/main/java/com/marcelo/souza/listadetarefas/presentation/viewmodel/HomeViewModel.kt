package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.model.HomeUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskFilter
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            _uiState.value = when (val result = repository.getTasks()) {
                is TaskResultViewData.Success -> HomeUiState.Success(result.data)
                is TaskResultViewData.Error -> HomeUiState.Error(result.error)
            }
        }
    }

    fun onFilterChange(filter: TaskFilter) {
        _selectedFilter.value = filter
    }

    fun onTaskCheckedChange(task: TaskViewData, isChecked: Boolean) {
        if (task.id.isBlank()) return

        viewModelScope.launch {
            when (repository.updateTaskCompletion(task.id, isChecked)) {
                is TaskResultViewData.Success -> {
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) {
                            state.copy(
                                tasks = state.tasks.map {
                                    if (it.id == task.id) it.copy(isCompleted = isChecked) else it
                                }
                            )
                        } else {
                            state
                        }
                    }
                }

                is TaskResultViewData.Error -> {
                    fetchTasks()
                }
            }
        }
    }
}
