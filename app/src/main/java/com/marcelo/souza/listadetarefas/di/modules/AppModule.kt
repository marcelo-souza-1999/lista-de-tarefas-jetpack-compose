package com.marcelo.souza.listadetarefas.di.modules

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DataSourceModule::class])
@ComponentScan("com.marcelo.souza.listadetarefas")
class AppModule