package com.app.valhalla.ui.launch

import android.content.Intent
import android.media.MediaPlayer
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
    private lateinit var mediaPlayer:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launch)
        binding = ActivityLaunchBinding.inflate(layoutInflater)


        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                initData()
            }
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.launch_music)
        if(mediaPlayer != null){
            mediaPlayer.start()
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
            bundle.putParcelable("stepGodData", Network.apiService.getStepGod().await())
            Log.d("TAGB",  bundle.toString())
            jumpToMainActivity(bundle)
        } catch (e: Exception) {
            Log.d("TAGAA",  e.message.toString())
        }
    }

    private fun jumpToMainActivity(bundle: Bundle) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("response", bundle))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer != null){
            mediaPlayer.stop()
        }

    }


}