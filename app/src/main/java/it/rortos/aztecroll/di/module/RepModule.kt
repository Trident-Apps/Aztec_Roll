package it.rortos.aztecroll.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.rortos.aztecroll.di.repository.DatabaseRepInt
import it.rortos.aztecroll.di.repository.DatabaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepModule {

    @Binds
    @Singleton
    fun bindRep(repository: DatabaseRepository): DatabaseRepInt
}