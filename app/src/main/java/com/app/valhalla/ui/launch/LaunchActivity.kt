package com.app.valhalla.ui.launch

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.valhalla.R
import com.app.valhalla.base.BaseActivity
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.databinding.ActivityLaunchBinding
import com.app.valhalla.ui.main.MainActivity
import com.app.valhalla.util.GifUtil
import com.app.valhalla.util.fadeIn
import com.app.valhalla.util.fadeOut
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    private val launchViewModel: LaunchViewModel by inject()
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        showLoading()

        //開頭動畫 前 加載資料
        launchViewModel.checkSavedDomain(this)

        launchViewModel.loadingViewStatePublisher.observe(
            this
        ){
            handleLoadingViewState(it)
        }


        //登入成功設定View
        launchViewModel.loginResultViewState.observe(
            this
        ) {
            when (it) {
                is LaunchViewModel.LoginResultViewState.LoginSuccessButtonState -> {

                    hideLoading()

                    binding.edEmail.apply {
                        fadeOut(500L)
                    }
                    binding.edNickname.apply {
                        fadeOut(500L)
                    }
                    binding.login.apply {
                        fadeOut(500L)
                    }

                    //開啟動畫Giff 啟動畫音效
                    mediaPlayer = MediaPlayer.create(this, R.raw.launch_music)
                    mediaPlayer.start()
                    binding.imgLogoAnimation.apply {
                        fadeIn(1500L)
                        setImageDrawable(
                            GifUtil.f_generateGif(
                                this@LaunchActivity,
                                R.drawable.logo_launch
                            )
                        )
                    }
                    binding.btnEnter.apply {
                        fadeIn(2000L)
                        isEnabled = true
                        text = getString(R.string.text_login_greeting, it.buttonString)
                    }
                }

                is LaunchViewModel.LoginResultViewState.LoginFailedState -> {
                    hideLoading()
                        binding.edEmail.apply {
                            fadeIn(1000L)
                        }
                        binding.edNickname.apply {
                            fadeIn(1000L)
                        }
                        binding.login.apply {
                            fadeIn(1000L)
                        }
                }
            }
        }



        val edEmail = binding.edEmail
        val edNickname = binding.edNickname
        val login = binding.login
        val loading = binding.imgLogoAnimation
        val resume = binding.btnResume
        val enter = binding.btnEnter

        launchViewModel.loginFormState.observe(this@LaunchActivity, Observer {
            if (it.isShow) {
                edEmail.visibility = View.VISIBLE
                edNickname.visibility = View.VISIBLE
                login.visibility = View.VISIBLE
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
        enter.setOnClickListener {
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

    private fun handleLoadingViewState(loadingViewState: BaseViewModel.LoadingViewState) {
        when (loadingViewState) {
            is BaseViewModel.LoadingViewState.ShowLoadingView -> {
                showLoading()
            }
            BaseViewModel.LoadingViewState.HideLoadingView -> {
                hideLoading()
            }
            BaseViewModel.LoadingViewState.MainActivityImageLoadingDone -> hideLoading()
        }
    }

}