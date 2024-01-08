package com.dn.vdp.base_mvvm.di

import com.dn.vdp.base_mvvm.data.retofit.PersonApi
import com.dn.vdp.base_mvvm.data.retofit.creator.RetrofitCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesPersonAPI(creator: RetrofitCreator): PersonApi{
        return creator.createService(PersonApi::class.java)
    }
}