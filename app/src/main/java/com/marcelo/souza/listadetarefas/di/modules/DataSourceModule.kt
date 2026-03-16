package com.marcelo.souza.listadetarefas.di.modules

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataSourceModule {
    @Single
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}