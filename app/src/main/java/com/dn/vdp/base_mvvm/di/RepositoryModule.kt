package com.dn.vdp.base_mvvm.di

import com.dn.vdp.base_mvvm.data.repository.PersonRepository
import com.dn.vdp.base_mvvm.data.repository.PersonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPersonRepository(repo: PersonRepositoryImpl): PersonRepository

}
