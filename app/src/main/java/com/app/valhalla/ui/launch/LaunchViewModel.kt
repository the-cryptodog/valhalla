package com.app.valhalla.ui.launch

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.ui.login.LoginFormState
import com.app.valhalla.util.Constant.BASE_URL
import com.app.valhalla.util.Constant.SAVED_URL_NAME
import com.app.valhalla.util.Constant.SAVED_URL_VALUE
import com.app.valhalla.util.Constant.SAVE_UID_NAME
import com.app.valhalla.util.PrefUtil
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LaunchViewModel(private val repository: MainRepository) : BaseViewModel() {

    //輸入框檢查
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    //登入結果
    private val _loginResultViewState = MutableLiveData<LoginResultViewState>()
    val loginResultViewState: LiveData<LoginResultViewState> = _loginResultViewState

    //註冊結果
    private val _registerResultViewState = MutableLiveData<RegisterResultViewState>()
    val registerResultViewState: LiveData<RegisterResultViewState> = _registerResultViewState

    //登入-註冊兩個模式切換
    private val _launchViewState = MutableLiveData<LaunchViewState>()
    val launchViewState: LiveData<LaunchViewState> = _launchViewState

    sealed class LoginResultViewState {
        data class AutoLoginSuccessButtonState(
            val buttonString: String
        ) : LoginResultViewState()

        data class AutoLoginFailedState(
            val isLoginFailed: Boolean
        ) : LoginResultViewState()
    }

    sealed class LaunchViewState {
        object LoginMode : LaunchViewState()
        object RegisterMode : LaunchViewState()
    }


    sealed class RegisterResultViewState {
        data class RegisterSuccessButtonState(
            val buttonString: String
        ) : RegisterResultViewState()

        data class RegisterFailedState(
            val errorMessage: String
        ) : RegisterResultViewState()
    }

    //登入或註冊時會先跑這個
    fun checkInputAndLoginOrRegistry(
        context: Context,
        email: String?,
        username: String?,
        password: String?
    ) {
        var isEmailValid = true
        var isNicknameValid = true
        var isPasswordValid = true

        email?.let {
            isEmailValid = AccountUtil.isEmailValid(it)
        }

        username?.let {
            isNicknameValid =
                AccountUtil.isNickNameValid(it) && _launchViewState.value is LaunchViewState.RegisterMode
        }

        password?.let {
            isPasswordValid = AccountUtil.isPasswordValid(it)
        }
        val isDataValid: Boolean = if (_launchViewState.value is LaunchViewState.LoginMode) {
            isEmailValid && isPasswordValid
        } else {
            isEmailValid && isPasswordValid && isNicknameValid
        }
        Log.d("QQQ", "isDataValid = $isDataValid")

        _loginForm.value = LoginFormState(
            emailError = if (!isEmailValid) R.string.invalid_username else null,
            nicknameError = if (!isNicknameValid) R.string.invalid_nickname else null,
            pwdError = if (!isPasswordValid) R.string.invalid_password else null,
            isDataValid = isDataValid
        )


        if (isDataValid) {
            if (_launchViewState.value == LaunchViewState.LoginMode) {
                Log.d("QQQ", "${_launchViewState.value == LaunchViewState.LoginMode}")
                checkMember(
                    context = context,
                    email,
                    password,
                    true
                )
            } else {
                Log.d("QQQ", "${_launchViewState.value == LaunchViewState.RegisterMode}")
                addMember(
                    context,
                    email!!,
                    username!!,
                    password!!
                )
            }
        }
    }


    fun genSound(context: Context) {
        MediaPlayer.create(context, R.raw.launch_music)?.start()
    }

    private fun checkMember(
        context: Context,
        email: String?,
        pwd: String?,
        isManualLogin: Boolean
    ) {
        viewModelScope.launch {
            loadingViewStatePublisher.value = LoadingViewState.ShowLoadingView("")
            withContext(Dispatchers.IO) {
                val result = repository.checkMember(getSavedUid(context), email, pwd)
                Log.d("FFF", "checkMember result＝$result")
                withContext(Dispatchers.Main) {
                    when (result) {
                        is MainDataSource.NetworkResult.Success -> {
                            //isHasSameProperty == "0"
                            if (result.data?.property_contents?.isHasSameProperty == "0") {
                                _loginResultViewState.value =
                                    LoginResultViewState.AutoLoginFailedState(isManualLogin)
                                _launchViewState.value = LaunchViewState.LoginMode
                            } else {
                                //成功
                                loadingViewStatePublisher.value = LoadingViewState.HideLoadingView
                                _loginResultViewState.value =
                                    LoginResultViewState.AutoLoginSuccessButtonState(result.data?.property_contents!!.nickname)
                            }
                        }
                        is MainDataSource.NetworkResult.Error -> {
                            loadingViewStatePublisher.value = LoadingViewState.HideLoadingView
                            ToastUtils.showLong(result.exception)
                        }
                    }
                }
            }
        }
    }

    fun addMember(context: Context, email: String, nickName: String, pwd: String) {
        loadingViewStatePublisher.value = LoadingViewState.ShowLoadingView("")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val result =
                    repository.addMember(getSavedUid(context), email, nickName, pwd)) {
                    is MainDataSource.NetworkResult.Success -> {
                        Log.d("FFF", "註冊回傳Success")
                        val resultMsg = result.data?.property_contents?.isHasSameProperty
                        if (resultMsg == "1") {
                            //新增成功 轉跳checkMember
                            checkMember(context, email, pwd, false)
                        } else {
                            //網路沒問題 新增未成功
                            withContext(Dispatchers.Main) {
                                _registerResultViewState.value =
                                    resultMsg?.let {
                                        RegisterResultViewState.RegisterFailedState(
                                            result.data.property_contents.hint
                                        )
                                    }
                            }
                        }
                    }
                    is MainDataSource.NetworkResult.Error -> {
                        //網路異常 新增失敗
                        withContext(Dispatchers.Main) {
                            _registerResultViewState.value =
                                RegisterResultViewState.RegisterFailedState(result.exception)
                        }
                    }
                }
            }
        }
    }


    private suspend fun getSavedUid(context: Context): String {
        val uid = PrefUtil(context).getString(SAVE_UID_NAME)
        Log.d("FFF", "id = $uid")
        return PrefUtil(context).getString(SAVE_UID_NAME).ifEmpty {
            val uniqueID = UUID.randomUUID().toString()
            PrefUtil(context).putString(SAVE_UID_NAME, uniqueID)
            uniqueID
        }
    }

    suspend fun clearLocalData(context: Context) {
        withContext(Dispatchers.IO) {
            PrefUtil(context).clear()
        }
    }

    // checkSavedDomain -> getNextApi -> checkMember ->addMember
    fun checkSavedDomain(context: Context) {
        viewModelScope.launch {
//            withContext(Dispatchers.IO) {  PrefUtil(context).clear()}
            if (PrefUtil(context).isEmpty(SAVED_URL_NAME)) {
                SAVED_URL_VALUE = BASE_URL
                val nextApi = async { getNextApi() }
                PrefUtil(context).putString(SAVED_URL_NAME, nextApi.await())
            } else {
                SAVED_URL_VALUE = PrefUtil(context).getString(SAVED_URL_NAME)
            }
            Log.d("FFF", SAVED_URL_VALUE)
            checkMember(context, null, null, false)
        }
    }

    private suspend fun getNextApi(): String {
        try {
            val result = repository.getNextApi()
            if (result is MainDataSource.NetworkResult.Success) {
                val resultUrl = result.data?.property_contents?.ApiList.toString()
                if (resultUrl != BASE_URL) {
                    return resultUrl
                }
            }
        } catch (e: Exception) {
            Log.d("LaunchViewModel", e.message.toString())
        }
        return BASE_URL
    }

    fun toggleLoginOrRegisterMode() {
        when (launchViewState.value) {
            is LaunchViewState.RegisterMode -> {
                _launchViewState.value = LaunchViewState.LoginMode
            }
            is LaunchViewState.LoginMode -> {
                _launchViewState.value = LaunchViewState.RegisterMode
            }
            null -> {

            }
        }
    }

}