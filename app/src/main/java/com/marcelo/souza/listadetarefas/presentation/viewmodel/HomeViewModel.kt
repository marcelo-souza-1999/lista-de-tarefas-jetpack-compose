package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_HIGH
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_LOW
import com.marcelo.souza.listadetarefas.data.utils.Constants.PRIORITY_MEDIUM
import com.marcelo.souza.listadetarefas.data.utils.Constants.USERS_COLLECTION
import com.marcelo.souza.listadetarefas.domain.model.DataError
import com.marcelo.souza.listadetarefas.domain.model.HomeUiState
import com.marcelo.souza.listadetarefas.domain.model.TaskFilter
import com.marcelo.souza.listadetarefas.domain.model.TaskResultViewData
import com.marcelo.souza.listadetarefas.domain.model.TaskViewData
import com.marcelo.souza.listadetarefas.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val repository: TaskRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _dialogError = MutableStateFlow<DataError?>(null)
    val dialogError = _dialogError.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    init {
        fetchUserName()
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            _dialogError.value = null
            _uiState.value = HomeUiState.Loading
            _uiState.value = when (val result = repository.getTasks()) {
                is TaskResultViewData.Success -> HomeUiState.Success(result.data)
                is TaskResultViewData.Error -> {
                    _dialogError.value = result.error
                    HomeUiState.Error(result.error)
                }
            }
        }
    }

    fun onFilterChange(filter: TaskFilter) {
        _selectedFilter.value = filter
    }

    fun onTaskCheckedChange(task: TaskViewData, isChecked: Boolean) {
        if (task.id.isBlank()) return

        viewModelScope.launch {
            when (val result = repository.updateTaskCompletion(task.id, isChecked)) {
                is TaskResultViewData.Success -> {
                    _dialogError.value = null
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) {
                            state.copy(tasks = state.tasks.map { if (it.id == task.id) it.copy(isCompleted = isChecked) else it })
                        } else {
                            state
                        }
                    }
                }

                is TaskResultViewData.Error -> _dialogError.value = result.error
            }
        }
    }

    fun onEditTask(task: TaskViewData) {
        if (task.id.isBlank()) return

        val editedTask = task.copy(priority = task.priority.nextPriority())

        viewModelScope.launch {
            when (val result = repository.updateTask(editedTask)) {
                is TaskResultViewData.Success -> {
                    _dialogError.value = null
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) {
                            state.copy(tasks = state.tasks.map { if (it.id == editedTask.id) editedTask else it })
                        } else {
                            state
                        }
                    }
                }

                is TaskResultViewData.Error -> _dialogError.value = result.error
            }
        }
    }

    fun onDeleteTask(task: TaskViewData) {
        if (task.id.isBlank()) return

        viewModelScope.launch {
            when (val result = repository.deleteTask(task.id)) {
                is TaskResultViewData.Success -> {
                    _dialogError.value = null
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) {
                            state.copy(tasks = state.tasks.filterNot { it.id == task.id })
                        } else {
                            state
                        }
                    }
                }

                is TaskResultViewData.Error -> _dialogError.value = result.error
            }
        }
    }

    fun dismissErrorDialog() {
        _dialogError.value = null
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                _userName.value = ""
                return@launch
            }

            val displayName = currentUser.displayName.orEmpty()
            if (displayName.isNotBlank()) {
                _userName.value = displayName
                return@launch
            }

            runCatching {
                firestore.collection(USERS_COLLECTION)
                    .document(currentUser.uid)
                    .get()
                    .await()
                    .getString("name")
                    .orEmpty()
            }.onSuccess { name ->
                _userName.value = name
            }.onFailure {
                _userName.value = currentUser.email.orEmpty()
            }
        }
    }

    private fun String.nextPriority(): String {
        return when (this) {
            PRIORITY_HIGH -> PRIORITY_MEDIUM
            PRIORITY_MEDIUM -> PRIORITY_LOW
            else -> PRIORITY_HIGH
        }
    }
}
