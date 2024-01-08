package com.dn.vdp.base_mvvm.presentation.fragments.navigate_main

import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.FragmentNavigateMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigateMainFragment :
    BaseFragment<NavigateMainViewModel, FragmentNavigateMainBinding>(R.layout.fragment_navigate_main) {
    override val binding by viewBindings(FragmentNavigateMainBinding::bind)
    override val viewModel by viewModels<NavigateMainViewModel>()

    override fun setupViews() {
        setupBottomNavigation()
    }

    override fun bindViewModel() {

    }

    private fun setupBottomNavigation() {
        activity?.findNavController(R.id.nav_host_fragment_fragment_main)?.let { navController ->
            val navView: BottomNavigationView = binding.navView
            navView.setupWithNavController(navController)
        }
    }
}