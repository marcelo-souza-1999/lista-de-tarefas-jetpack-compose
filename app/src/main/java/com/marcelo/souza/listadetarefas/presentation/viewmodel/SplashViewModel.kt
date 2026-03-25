package com.marcelo.souza.listadetarefas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import com.marcelo.souza.listadetarefas.presentation.navigation.model.HomeKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.LoginKey
import com.marcelo.souza.listadetarefas.presentation.navigation.model.NavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    private val authRepository: AuthenticateRepository
) : ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                delay(SPLASH_DELAY_MILLIS)
                val isLoggedIn = authRepository.isUserLoggedIn()
                val destination = if (isLoggedIn) HomeKey else LoginKey
                _navigationEvent.send(NavigationEvent.NavigateAndClear(destination))
            } catch (e: Exception) {
                _navigationEvent.send(NavigationEvent.NavigateAndClear(LoginKey))
            }
        }
    }

    companion object {
        private const val SPLASH_DELAY_MILLIS = 1500L
    }
}