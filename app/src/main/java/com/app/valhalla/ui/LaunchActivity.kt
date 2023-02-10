package com.app.valhalla.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityDrawLotsBinding
import com.app.valhalla.data.api.Network
import com.app.valhalla.databinding.ActivityLaunchBinding
import com.app.valhalla.ui.main.MainActivity
import com.app.valhalla.util.GifUtil
import com.blankj.utilcode.util.BarUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class LaunchActivity : BaseActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launch)
        binding = ActivityLaunchBinding.inflate(layoutInflater)


        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                initData()
            }
        }

        binding.imgLogoAnimation.setImageDrawable(
            GifUtil.f_generateGif(
                this,
                R.drawable.logo_launch
            )
        )

    }

    private suspend fun initData() {
        val bundle = Bundle()
        bundle.putParcelable("data" ,Network.apiService.getDefault().await())
        jumpToMainActivity(bundle)
        Log.d("TAGB", bundle.toString())
    }

    private fun jumpToMainActivity(bundle: Bundle) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("response", bundle))
        finish()
    }


}