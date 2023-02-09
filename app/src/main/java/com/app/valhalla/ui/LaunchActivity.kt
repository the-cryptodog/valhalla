package com.app.valhalla.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.databinding.ActivityDrawLotsBinding
import com.app.valhalla.databinding.ActivityLaunchBinding
import com.app.valhalla.ui.main.MainActivity
import com.app.valhalla.util.GifUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        binding = ActivityLaunchBinding.inflate(layoutInflater)


        lifecycleScope.launch {
            delay(3000)
            jumpToMainActivity()
        }

        binding.imgLogoAnimation.setImageDrawable(
            GifUtil.f_generateGif(
                this,
                R.drawable.loading_logo
            )
        )

    }

    fun jumpToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}