package com.dn.vdp.base_mvvm.data.roomdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAll(): List<Task>

    @Insert
    suspend fun insertAll(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)
}