package com.dn.vdp.base_mvvm.presentation.activities.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.dn.vdp.base_mvvm.databinding.ActivitySplashBinding
import com.dn.vdp.base_mvvm.presentation.activities.main.MainActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity @Inject constructor() : Activity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.statusBarColor = ContextCompat.getColor(this, android.R.color.holo_blue_light)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000L)
    }
}