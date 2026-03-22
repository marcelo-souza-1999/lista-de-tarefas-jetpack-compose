package com.marcelo.souza.listadetarefas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthenticateRepositoryImplTest {
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private val user = mockk<FirebaseUser>()

    private lateinit var repository: AuthenticateRepositoryImpl

    @Before
    fun setup() {
        repository = AuthenticateRepositoryImpl(firebaseAuth, firestore)
    }

    @Test
    fun `isUserLoggedIn should return true when firebase current user exists`() {
        every { firebaseAuth.currentUser } returns user

        assertTrue(repository.isUserLoggedIn())
    }

    @Test
    fun `getCurrentUserId should return firebase uid`() {
        every { firebaseAuth.currentUser } returns user
        every { user.uid } returns "uid-1"

        assertEquals("uid-1", repository.getCurrentUserId())
    }

    @Test
    fun `logout should call signOut`() {
        every { firebaseAuth.signOut() } just runs

        kotlinx.coroutines.runBlocking { repository.logout() }

        verify(exactly = 1) { firebaseAuth.signOut() }
    }
}
