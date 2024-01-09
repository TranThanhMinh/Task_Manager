package com.dn.vdp.base_mvvm.data.roomdata

import javax.inject.Inject

class DatabaseHelperImpl @Inject constructor(private val gfgDatabase: TaskDao) : DatabaseHelper {
    override suspend fun getTask(): List<Task> = gfgDatabase.getAll()
    override suspend fun insertAll(task: Task) = gfgDatabase.insertAll(task)
    override suspend fun update(task: Task) {
        return gfgDatabase.update(task)
    }
    override suspend fun delete(task: Task) {
        return gfgDatabase.delete(task)
    }
}