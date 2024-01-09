package com.dn.vdp.base_mvvm.data.roomdata

interface DatabaseHelper{
    suspend fun  getTask():List<Task>
    suspend fun  insertAll(task: Task)
    suspend fun  update(task: Task)
    suspend fun  delete(task: Task)
}