package com.dn.vdp.base_mvvm.presentation.fragments.task

import androidx.lifecycle.viewModelScope
import com.dn.vdp.base_module.presentation.BaseViewModel
import com.dn.vdp.base_mvvm.data.roomdata.DatabaseHelper
import com.dn.vdp.base_mvvm.data.roomdata.DatabaseHelperImpl
import com.dn.vdp.base_mvvm.data.roomdata.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(val dbHelper : DatabaseHelperImpl) : BaseViewModel() {

    val actionFlow: MutableStateFlow<Tasks> = MutableStateFlow(Tasks.Empty)
    val stateFlowAccount: StateFlow<Tasks> = actionFlow
     fun fetchTask() {
        viewModelScope.launch {
            try {
                val allTask = dbHelper.getTask()
                actionFlow.value = Tasks.getTask(allTask as ArrayList)
                // here you have your CoursesFromDb
            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun fetchTaskDateToDate(fromDate:String,toDate:String) {
        viewModelScope.launch {
            try {
                val allTask = dbHelper.getTaskDateToDate(fromDate,toDate)
                actionFlow.value = Tasks.getTask(allTask as ArrayList)
                // here you have your CoursesFromDb
            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun fetchTaskHistoryDateToDate(fromDate:String,toDate:String) {
        viewModelScope.launch {
            try {
                val allTask = dbHelper.getTaskHistoryDateToDate(fromDate,toDate)
                actionFlow.value = Tasks.getTask(allTask as ArrayList)
                // here you have your CoursesFromDb
            } catch (e: Exception) {
                // handler error
            }
        }
    }

   fun insert(task: Task){
       viewModelScope.launch {
           dbHelper.insertAll(task)
       }
   }

    fun update(task: Task){
        viewModelScope.launch {
            dbHelper.update(task)
        }
    }

    fun delete(task: Task){
        viewModelScope.launch {
            dbHelper.delete(task)
        }
    }
    sealed class Tasks {
        object Empty : Tasks()
        class getTask(
            val list: ArrayList<Task>
        ) :
            Tasks()
    }
}