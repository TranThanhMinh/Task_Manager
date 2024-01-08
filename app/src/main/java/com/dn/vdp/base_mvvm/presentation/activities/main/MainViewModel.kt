package com.dn.vdp.base_mvvm.presentation.activities.main

import androidx.lifecycle.viewModelScope
import com.dn.vdp.base_module.presentation.BaseViewModel
import com.dn.vdp.base_mvvm.data.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val personRepo: PersonRepository
) : BaseViewModel() {

    fun test() {
        personRepo.getPersonInfo()
            .onEach { Timber.log(1000, "#######" + it.toString()) }
            .catch { Timber.log(1000, "#######" + it.message.toString()) }
            .launchIn(viewModelScope)
    }
}