package com.app.valhalla.ui.launch

import android.app.AlertDialog
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
import com.app.valhalla.util.fadeIn
import com.app.valhalla.util.fadeOut
import com.blankj.utilcode.util.ToastUtils
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
        ) {
            handleLoadingViewState(it)
        }
        launchViewModel.launchViewState.observe(
            this
        ) {
            clearAllInput()
            if (it is LaunchViewModel.LaunchViewState.LoginMode) {
                binding.edNickname.apply {
                    visibility = View.GONE
                }
                binding.login.visibility = View.VISIBLE
                binding.register.visibility = View.GONE
                binding.switcher.text = "我要註冊"
            }
            if (it is LaunchViewModel.LaunchViewState.RegisterMode) {
                binding.edNickname.apply {
                    visibility = View.VISIBLE
                }
                binding.login.visibility = View.GONE
                binding.register.visibility = View.VISIBLE
                binding.switcher.text = "我要登入"
            }
        }

        launchViewModel.registerResultViewState.observe(
            this
        ) {
            if (it is LaunchViewModel.RegisterResultViewState.RegisterFailedState) {
                ToastUtils.showLong("註冊失敗 is_hassameproperty=$it.errorMessage")
                hideLoading()
            }
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
                    binding.edPwd.apply {
                        fadeOut(500L)
                    }
                    binding.login.apply {
                        fadeOut(500L)
                    }
                    binding.register.apply {
                        fadeOut(500L)
                    }
                    binding.switcher.apply {
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
                    if (it.isLoginFailed) {
                        showAlertDialog()
                    }
                    hideLoading()
                    binding.edEmail.apply {
                        fadeIn(1000L)
                    }
                    binding.edPwd.apply {
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
        val register = binding.register
        val edPwd = binding.edPwd
        val loading = binding.imgLogoAnimation
        val resume = binding.btnResume
        val enter = binding.btnEnter
        val switcher = binding.switcher

        launchViewModel.loginFormState.observe(this@LaunchActivity, Observer { loginState ->

            Log.d(
                "QQQ",
                "${loginState.emailError} , ${loginState.nicknameError} , ${loginState.pwdError}"
            )

            if (loginState.emailError != null) {
                edEmail.error = getString(loginState.emailError)
            }

            if (loginState.nicknameError != null) {
                edNickname.error = getString(loginState.nicknameError)
            }

            if (loginState.pwdError != null) {
                edPwd.error = getString(loginState.pwdError)
            }
        })

        edEmail.addTextChangedListener {
            edEmail.error = null
        }

        edNickname.addTextChangedListener {
            edNickname.error = null
        }

        edPwd.addTextChangedListener {
            edPwd.error = null
        }

        switcher.setOnClickListener {
            launchViewModel.toggleLoginOrRegisterMode()
        }

        register.setOnClickListener {
            launchViewModel.checkInputAndLoginOrRegistry(
                this,
                edEmail.text.toString(),
                edNickname.text.toString(),
                edPwd.text.toString()
            )
        }
        resume.setOnClickListener {
            lifecycleScope.launch {
                launchViewModel.clearLocalData(this@LaunchActivity)
            }
            this@LaunchActivity.restartActivity()
        }
        enter.setOnClickListener {
            jumpToMainActivity()
        }

        login.setOnClickListener {
            Log.d(
                "QQQ",
                "${edEmail.text.toString()} , ${edNickname.text.toString()} , ${edPwd.text.toString()}"
            )
            launchViewModel.checkInputAndLoginOrRegistry(
                this,
                edEmail.text.toString(),
                edNickname.text.toString(),
                edPwd.text.toString()
            )
        }
    }


    override fun getViewBinding(): ActivityLaunchBinding {
        return ActivityLaunchBinding.inflate(layoutInflater)
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this@LaunchActivity)
        alertDialogBuilder.setTitle("你還沒有成為主委喔")
        alertDialogBuilder.setMessage("請確認您的主委資格。")

        alertDialogBuilder.setPositiveButton("確定") { dialog, _ ->
            // 在此處放置按下「確定」按鈕後的處理邏輯
            dialog.dismiss() // 關閉 AlertDialog
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun jumpToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        // 確保在使用 mediaPlayer 之前檢查是否已初始化
        if (::mediaPlayer.isInitialized) {
            // 在這裡釋放 mediaPlayer 資源
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
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

    private fun restartActivity() {
        val intent = Intent(this, LaunchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun clearAllInput() {
        binding.edNickname.text.clear()
        binding.edPwd.text.clear()
        binding.edEmail.text.clear()
    }
}