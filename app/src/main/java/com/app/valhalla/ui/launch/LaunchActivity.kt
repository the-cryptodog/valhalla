package com.app.valhalla.ui.launch

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.databinding.ActivityLaunchBinding
import com.app.valhalla.ui.main.MainActivity
import com.app.valhalla.util.GifUtil
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    private val launchViewModel: LaunchViewModel by inject()
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //開頭動畫 前 加載資料
        launchViewModel.initData()


        //開啟動畫音效
        mediaPlayer = MediaPlayer.create(this, R.raw.launch_music)
        if (mediaPlayer != null) {
            mediaPlayer.start()
        }
        //開啟動畫Giff
        binding.imgLogoAnimation.setImageDrawable(
            GifUtil.f_generateGif(
                this,
                R.drawable.logo_launch
            )
        )
        //觀察 網路請求成功後回調值 轉跳
        launchViewModel.loadingViewStatePublisher.observe(
            this
        ) {
            Log.d("LaunchActivity", it.javaClass.name) //僅有 Success 或 Error 兩個類
            jumpToMainActivity()
        }
    }


    override fun getViewBinding(): ActivityLaunchBinding {
        return ActivityLaunchBinding.inflate(layoutInflater)
    }

    private fun jumpToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer.stop()
        }
    }

    //待修正 ：mediaPlayer 寫進 Base 類（？

}