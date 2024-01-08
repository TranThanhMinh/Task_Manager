package com.dn.vdp.base_mvvm.presentation.fragments.dashboard

import androidx.fragment.app.viewModels
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment :
    BaseFragment<DashboardViewModel, FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    override val binding by viewBindings(FragmentDashboardBinding::bind)
    override val viewModel by viewModels<DashboardViewModel>()

    override fun setupViews() {

    }

    override fun bindViewModel() {

    }
}
