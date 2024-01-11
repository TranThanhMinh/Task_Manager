package com.dn.vdp.base_mvvm.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dn.vdp.base_mvvm.data.roomdata.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDataModule {
    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = run {
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE Task ADD COLUMN reason TEXT")
//            }
//        }
//
//        val MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE Task ADD COLUMN complete INTEGER")
//            }
//        }
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "roomdata"
        ).build() // The reason we can construct a database for the repo
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: AppDatabase) = db.TaskDao()
}