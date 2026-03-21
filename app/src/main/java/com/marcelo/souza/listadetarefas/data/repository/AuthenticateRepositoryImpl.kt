package com.marcelo.souza.listadetarefas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_NAME
import com.marcelo.souza.listadetarefas.data.utils.Constants.USERS_COLLECTION
import com.marcelo.souza.listadetarefas.domain.repository.AuthenticateRepository
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class AuthenticateRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticateRepository {

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override suspend fun fetchUserName(): String {
        val user = firebaseAuth.currentUser ?: return ""

        return runCatching {
            firestore.collection(USERS_COLLECTION)
                .document(user.uid)
                .get()
                .await()
                .getString(FIELD_NAME)
                .orEmpty()
        }.getOrDefault(user.email.orEmpty())
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return runCatching {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit> {
        return runCatching {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val userId = result.user?.uid
                ?: error("User ID is null")

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(mapOf(FIELD_NAME to name))
                .await()
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}