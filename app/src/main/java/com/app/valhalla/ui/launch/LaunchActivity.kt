package com.app.valhalla.ui.launch

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.data.BaseViewModel
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
        launchViewModel.checkSavedDomain(this)


        //開啟動畫音效
        mediaPlayer = MediaPlayer.create(this, R.raw.launch_music)
        mediaPlayer.start()

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
            when (it) {
                is BaseViewModel.LoadingViewState.HideLoadingView -> {
                    Log.d("LaunchActivity", it.javaClass.name) //僅有 Success 或 Error 兩個類
                    jumpToMainActivity()
                }
                is BaseViewModel.LoadingViewState.MainActivityImageLoadingDone -> TODO()
                is BaseViewModel.LoadingViewState.ShowLoadingView -> TODO()
            }
        }

        val edEmail = binding.edEmail
        val edNickname = binding.edNickname
        val login = binding.login
        val loading = binding.imgLogoAnimation
        val resume = binding.btnResume

        launchViewModel.loginFormState.observe(this@LaunchActivity, Observer {
            if(it.isShow){
                edEmail.visibility =View.VISIBLE
                edNickname.visibility =View.VISIBLE
                login.visibility =View.VISIBLE
            }


            val loginState = it ?: return@Observer

            // disable login button unless both edEmail / edNickname is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                edEmail.error = getString(loginState.emailError)
            }
            if (loginState.nicknameError != null) {
                edNickname.error = getString(loginState.nicknameError)
            }
        })


        edEmail.addTextChangedListener {
            launchViewModel.loginDataChanged(
                edEmail.text.toString(),
                edNickname.text.toString()
            )
        }

        edNickname.apply {
            addTextChangedListener {
                launchViewModel.loginDataChanged(
                    edEmail.text.toString(),
                    edNickname.text.toString()
                )
            }
        }

        login.setOnClickListener {
            launchViewModel.addMember(
                context = this,
                edEmail.text.toString(),
                edNickname.text.toString()
            )
        }
        resume.setOnClickListener {
            lifecycleScope.launch {
                launchViewModel.clearLocalData(this@LaunchActivity)
            }
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