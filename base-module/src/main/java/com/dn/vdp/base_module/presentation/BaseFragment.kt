package com.dn.vdp.base_module.presentation

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>(@LayoutRes layoutRes: Int) :
    Fragment(layoutRes) {
    protected abstract val binding: VB
    protected abstract val viewModel: VM


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
    }

    @MainThread
    protected abstract fun setupViews()

    @MainThread
    protected abstract fun bindViewModel()
}
