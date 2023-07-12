package com.app.valhalla.ui.launch

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
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


class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    private val launchViewModel: LaunchViewModel by inject()
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoading()

        //開頭動畫 前 加載資料
        launchViewModel.checkSavedDomain(this)

        //設定讀取圖示觀察
        handleLoadingObservation()

        //設定基本元件邏輯
        handleButtonAndEditTextViewState(binding)


    }


    override fun getViewBinding(): ActivityLaunchBinding {
        return ActivityLaunchBinding.inflate(layoutInflater)
    }

    private fun handleLoadingObservation() {
        launchViewModel.loadingViewStatePublisher.observe(
            this
        ) {
            handleLoadingViewState(it)
        }
    }

    private fun handleButtonAndEditTextViewState(binding: ActivityLaunchBinding) {
        with(binding) {
            //切換註冊與登入
            launchViewModel.launchViewState.observe(
                this@LaunchActivity
            ) {
                clearAllInput()
                if (it is LaunchViewModel.LaunchViewState.LoginMode) {
                    edNickname.apply {
                        visibility = View.GONE
                    }
                    login.visibility = View.VISIBLE
                    register.visibility = View.GONE
                    switcher.text = "我要註冊"
                }
                if (it is LaunchViewModel.LaunchViewState.RegisterMode) {
                    edNickname.apply {
                        visibility = View.VISIBLE
                    }
                    login.visibility = View.GONE
                    register.visibility = View.VISIBLE
                    switcher.text = "我要登入"
                }
            }
            //輸入框檢查
            launchViewModel.loginFormState.observe(this@LaunchActivity, Observer { loginState ->
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

            //註冊結果
            launchViewModel.registerResultViewState.observe(
                this@LaunchActivity
            ) {
                if (it is LaunchViewModel.RegisterResultViewState.RegisterFailedState) {
                    ToastUtils.showLong("註冊失敗 is_hassameproperty=$it.errorMessage")
                    hideLoading()
                }
            }

            //自動登入結果
            launchViewModel.loginResultViewState.observe(
                this@LaunchActivity
            ) {
                hideLoading()
                when (it) {
                    //自動登入成功，畫面切換為預備進入遊戲畫面
                    is LaunchViewModel.LoginResultViewState.LoginSuccessButtonState -> {
                        edEmail.apply { fadeOut(500L) }
                        edNickname.apply { fadeOut(500L) }
                        edPwd.apply { fadeOut(500L) }
                        login.apply { fadeOut(500L) }
                        register.apply { fadeOut(500L) }
                        switcher.apply { fadeOut(500L) }

                        //開啟動畫Giff 啟動畫音效
                        mediaPlayer = MediaPlayer.create(this@LaunchActivity, R.raw.launch_music)
                        mediaPlayer.start()
                        imgLogoAnimation.apply {
                            fadeIn(1500L)
                            setImageDrawable(
                                GifUtil.f_generateGif(
                                    this@LaunchActivity,
                                    R.drawable.logo_launch
                                )
                            )
                        }
                        btnEnter.apply {
                            fadeIn(2000L)
                            isEnabled = true
                            text = getString(R.string.text_login_greeting, it.buttonString)
                        }
                    }

                    //自動登入失敗，畫面切換為登入畫面
                    is LaunchViewModel.LoginResultViewState.LoginFailedState -> {
                        if (it.isAutoLoginFailed) {
                            ToastUtils.showLong(it.hint)
                            edEmail.apply {
                                fadeIn(1000L)
                            }
                            edPwd.apply {
                                fadeIn(1000L)
                            }
                            login.apply {
                                fadeIn(1000L)
                            }
                        }else{
                            ToastUtils.showLong(it.hint)
                        }
                    }
                }
            }

            //新增改動監聽
            edEmail.addTextChangedListener {
                edEmail.error = null
            }

            edNickname.addTextChangedListener {
                edNickname.error = null
            }

            edPwd.addTextChangedListener {
                edPwd.error = null
            }

            //點擊事件
            switcher.setOnClickListener {
                launchViewModel.toggleLoginOrRegisterMode()
            }

            register.setOnClickListener {
                launchViewModel.checkInputAndLoginOrRegistry(
                    this@LaunchActivity,
                    edEmail.text.toString(),
                    edNickname.text.toString(),
                    edPwd.text.toString()
                )
            }
            btnResume.setOnClickListener {
                lifecycleScope.launch {
                    launchViewModel.clearLocalData(this@LaunchActivity)
                }
                this@LaunchActivity.restartActivity()
            }
            btnEnter.setOnClickListener {
                jumpToMainActivity()
            }

            login.setOnClickListener {
                launchViewModel.checkInputAndLoginOrRegistry(
                    this@LaunchActivity,
                    edEmail.text.toString(),
                    edNickname.text.toString(),
                    edPwd.text.toString()
                )
            }
        }
    }

    //api回調提醒dialog
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

    //處理loading UI
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