package com.dn.vdp.base_mvvm.data.roomdata

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TaskDao(): TaskDao
}