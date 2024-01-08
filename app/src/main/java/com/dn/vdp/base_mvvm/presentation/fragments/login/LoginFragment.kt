package com.dn.vdp.base_mvvm.presentation.fragments.login

import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.clicks
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.AuthNavigationDirections
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {
    override val binding by viewBindings(FragmentLoginBinding::bind)
    override val viewModel by viewModels<LoginViewModel>()

    override fun setupViews() {
        binding.loginRegisterBtn.clicks {
            findNavController().navigate(AuthNavigationDirections.actionAuthToMain())
        }
    }

    override fun bindViewModel() {
    }
}