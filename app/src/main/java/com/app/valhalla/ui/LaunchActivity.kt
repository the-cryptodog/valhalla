package com.app.valhalla.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.data.api.Network
import com.app.valhalla.databinding.ActivityLaunchBinding
import com.app.valhalla.ui.main.MainActivity
import com.app.valhalla.util.GifUtil
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
            withContext(Dispatchers.IO) {
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
        try {
            bundle.putParcelable("data", Network.apiService.getDefault().await())
            Log.d("TAGB", bundle.toString())
            jumpToMainActivity(bundle)
        } catch (e: Exception) {
            Log.d("TAGB",  e.message.toString())
        }
    }

    private fun jumpToMainActivity(bundle: Bundle) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("response", bundle))
        finish()
    }


}