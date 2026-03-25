package com.marcelo.souza.listadetarefas.data.repository

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.marcelo.souza.listadetarefas.data.utils.Constants.FIELD_NAME
import com.marcelo.souza.listadetarefas.data.utils.Constants.USERS_COLLECTION
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthenticateRepositoryImplTest {

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val user = mockk<FirebaseUser>(relaxed = true)
    private val collectionRef = mockk<CollectionReference>()
    private val documentRef = mockk<DocumentReference>()

    private lateinit var repository: AuthenticateRepositoryImpl

    @Before
    fun setup() {
        every { firestore.collection(USERS_COLLECTION) } returns collectionRef
        every { collectionRef.document(any()) } returns documentRef
        repository = AuthenticateRepositoryImpl(firebaseAuth, firestore)
    }

    @Test
    fun `isUserLoggedIn should return true when user exists`() {
        every { firebaseAuth.currentUser } returns user
        assertTrue(repository.isUserLoggedIn())
    }

    @Test
    fun `isUserLoggedIn should return false when user is null`() {
        every { firebaseAuth.currentUser } returns null
        assertFalse(repository.isUserLoggedIn())
    }

    @Test
    fun `getCurrentUserId should return uid when user is logged`() {
        every { firebaseAuth.currentUser } returns user
        every { user.uid } returns "marcelo-123"
        assertEquals("marcelo-123", repository.getCurrentUserId())
    }

    @Test
    fun `fetchUserName should return name from firestore when successful`() = runTest {
        val uid = "123"
        every { firebaseAuth.currentUser } returns user
        every { user.uid } returns uid

        val snapshot = mockk<DocumentSnapshot>()
        every { snapshot.getString(FIELD_NAME) } returns "Marcelo Souza"

        val tcs = TaskCompletionSource<DocumentSnapshot>()
        tcs.setResult(snapshot)
        every { documentRef.get() } returns tcs.task

        val name = repository.fetchUserName()
        assertEquals("Marcelo Souza", name)
    }

    @Test
    fun `fetchUserName should return email fallback when firestore fails`() = runTest {
        every { firebaseAuth.currentUser } returns user
        every { user.email } returns "teste@email.com"

        val tcs = TaskCompletionSource<DocumentSnapshot>()
        tcs.setException(Exception("Erro Firestore"))
        every { documentRef.get() } returns tcs.task

        val name = repository.fetchUserName()
        assertEquals("teste@email.com", name)
    }

    @Test
    fun `login should return Success when firebase auth completes`() = runTest {
        val tcs = TaskCompletionSource<AuthResult>()
        tcs.setResult(mockk())
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns tcs.task

        val result = repository.login("email@test.com", "123456")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `register should create user and save name in firestore`() = runTest {
        val authResult = mockk<AuthResult>()
        every { authResult.user } returns user
        every { user.uid } returns "new_uid"

        val authTcs = TaskCompletionSource<AuthResult>()
        authTcs.setResult(authResult)
        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTcs.task

        val firestoreTcs = TaskCompletionSource<Void>()
        firestoreTcs.setResult(null)
        every { documentRef.set(any()) } returns firestoreTcs.task

        val result = repository.register("Marcelo", "marcelo@dev.com", "senha123")

        assertTrue(result.isSuccess)
        verify { documentRef.set(mapOf(FIELD_NAME to "Marcelo")) }
    }

    @Test
    fun `register should delete user (rollback) when firestore fails`() = runTest {
        val authResult = mockk<AuthResult>()
        every { authResult.user } returns user
        every { user.uid } returns "uid_to_delete"

        val authTcs = TaskCompletionSource<AuthResult>()
        authTcs.setResult(authResult)
        every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns authTcs.task

        val firestoreTcs = TaskCompletionSource<Void>()
        firestoreTcs.setException(Exception("Firestore Down"))
        every { documentRef.set(any()) } returns firestoreTcs.task

        val deleteTcs = TaskCompletionSource<Void>()
        deleteTcs.setResult(null)
        every { user.delete() } returns deleteTcs.task

        val result = repository.register("Marcelo", "email@test.com", "123")

        assertTrue(result.isFailure)
        coVerify { user.delete() }
    }

    @Test
    fun `logout should call firebase signOut`() = runTest {
        repository.logout()
        verify { firebaseAuth.signOut() }
    }
}