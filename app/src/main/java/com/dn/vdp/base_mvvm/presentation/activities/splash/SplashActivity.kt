package com.dn.vdp.base_mvvm.presentation.activities.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.ActivitySplashBinding
import com.dn.vdp.base_mvvm.presentation.activities.main.MainActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity @Inject constructor() : Activity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.white)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000L)

    }

    fun changeStatusBarColor(color: Int) {
        val window: Window = this?.window!!
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //  set status text dark
        var flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags  //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}