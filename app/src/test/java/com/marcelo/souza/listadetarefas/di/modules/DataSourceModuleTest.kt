package com.marcelo.souza.listadetarefas.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertTrue
import org.junit.Test

class DataSourceModuleTest {

    private val module = DataSourceModule()

    @Test
    fun `provideFirebaseFirestore should return instance`() {
        val instance = module.provideFirebaseFirestore()

        assertTrue(instance is FirebaseFirestore)
    }

    @Test
    fun `provideFirebaseAuth should return instance`() {
        val instance = module.provideFirebaseAuth()

        assertTrue(instance is FirebaseAuth)
    }
}
