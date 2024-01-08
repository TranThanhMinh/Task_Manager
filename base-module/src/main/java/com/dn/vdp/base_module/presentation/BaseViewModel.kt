package com.dn.vdp.base_module.presentation

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {
    private val lazyJobs = lazy(LazyThreadSafetyMode.NONE) { HashSet<Job>() }
    private val jobs by lazyJobs
    private val _inputAction = MutableSharedFlow<BaseAction>()
    protected val inputAction: Flow<BaseAction> = _inputAction

    protected fun pushAction(action: BaseAction) {
        _inputAction.tryEmit(action)
    }

    @MainThread
    protected fun Job.addToJobDispose() = apply { jobs += this }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        if (lazyJobs.isInitialized()) {
            jobs.forEach { it.cancel() }
            jobs.clear()
        }
    }
}
