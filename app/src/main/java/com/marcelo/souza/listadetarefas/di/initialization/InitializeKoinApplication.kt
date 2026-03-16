package com.marcelo.souza.listadetarefas.di.initialization

import android.app.Application
import com.marcelo.souza.listadetarefas.di.modules.AppModule
import com.marcelo.souza.listadetarefas.di.modules.DataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class InitializeKoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                AppModule().module,
                DataSourceModule().module
            )
        }
    }
}