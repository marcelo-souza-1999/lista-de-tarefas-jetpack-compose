package com.marcelo.souza.listadetarefas.domain.repository

interface AuthenticateRepository {

    fun isUserLoggedIn(): Boolean

    fun getCurrentUserId(): String?

    suspend fun fetchUserName(): String

    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit>

    suspend fun logout()
}