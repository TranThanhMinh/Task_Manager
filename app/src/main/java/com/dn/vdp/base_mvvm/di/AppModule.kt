package com.dn.vdp.base_mvvm.di

import android.content.Context
import com.dn.vdp.base_module.utils.Network
import com.dn.vdp.base_module.utils.NetworkConnectivity
import com.dn.vdp.base_mvvm.data.local.prefrence.AppPreferencesImpl
import com.dn.vdp.base_mvvm.data.local.prefrence.AppSharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideNetworkAppPreferences(@ApplicationContext context: Context): AppSharedPreferences {
        return AppPreferencesImpl(context)
    }
}