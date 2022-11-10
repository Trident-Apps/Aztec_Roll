package it.rortos.realflig.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.rortos.realflig.di.repository.DatabaseRepInt
import it.rortos.realflig.di.repository.DatabaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepModule {

    @Binds
    @Singleton
    fun bindRep(repository: DatabaseRepository): DatabaseRepInt
}