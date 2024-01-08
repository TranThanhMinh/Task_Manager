package com.dn.vdp.base_mvvm.presentation.fragments.notifications

import androidx.fragment.app.viewModels
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<NotificationsViewModel, FragmentNotificationsBinding>(
    R.layout.fragment_notifications
) {

    override val binding by viewBindings(FragmentNotificationsBinding::bind)
    override val viewModel by viewModels<NotificationsViewModel>()

    override fun setupViews() {

    }

    override fun bindViewModel() {

    }
}