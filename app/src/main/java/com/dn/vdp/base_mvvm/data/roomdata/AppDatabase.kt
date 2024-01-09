package com.dn.vdp.base_mvvm.data.roomdata

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TaskDao(): TaskDao
}