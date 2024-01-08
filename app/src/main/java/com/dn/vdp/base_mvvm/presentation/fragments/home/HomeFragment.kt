package com.dn.vdp.base_mvvm.presentation.fragments.home

import androidx.fragment.app.viewModels
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    override val binding by viewBindings(FragmentHomeBinding::bind)
    override val viewModel by viewModels<HomeViewModel>()

    override fun setupViews() {

    }

    override fun bindViewModel() {

    }
}