package com.app.valhalla.ui.launch

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.Result
import com.app.valhalla.ui.login.LoggedInUserView
import com.app.valhalla.ui.login.LoginFormState
import com.app.valhalla.ui.login.LoginResult
import com.app.valhalla.ui.main.MainViewModel
import com.app.valhalla.util.Constant.BASE_URL
import com.app.valhalla.util.Constant.SAVED_URL_NAME
import com.app.valhalla.util.Constant.SAVED_URL_VALUE
import com.app.valhalla.util.Constant.SAVE_UID_NAME
import com.app.valhalla.util.PrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class LaunchViewModel(private val repository: MainRepository) : BaseViewModel() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(nicknameError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return true
    }


    fun genSound(context: Context) {
        MediaPlayer.create(context, R.raw.launch_music)?.start()
    }

    private fun checkMember(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.checkMember(getSavedUid(context))
                Log.d("FFF", "checkMember ＩＤ＝$result")
                if (result is MainDataSource.NetworkResult.Success) {
                    if (result.data?.property_contents?.isHasSameProperty == "0") {
                        Log.d("FFF", "重新 登入 畫面" + result.data.toString())
                        _loginForm.postValue(LoginFormState(isShow = true))
                    } else {
                        Log.d("FFF", "已有帳戶 登入" + result.data.toString())
                    }
                }
            }
        }
    }

    fun test(){
        123456
    }

     fun addMember(context: Context,email:String,nickName:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("FFF", "還沒有帳戶")
                val result = repository.addMember(getSavedUid(context),email,nickName)
                if (result is MainDataSource.NetworkResult.Success) {
                    //新增成功 轉跳checkMember
                    checkMember(context)
                } else {

                }
            }
        }
    }


    private suspend fun getSavedUid(context: Context): String {
        return PrefUtil(context).getString(SAVE_UID_NAME).ifEmpty {
            val uniqueID = UUID.randomUUID().toString()
            Log.d("FFF", "建立新ＩＤ＝$uniqueID")
            PrefUtil(context).putString(SAVE_UID_NAME, uniqueID)
            uniqueID
        }
    }
    suspend fun clearLocalData(context: Context) {
        withContext(Dispatchers.IO){
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
            checkMember(context)
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

    fun initData() {
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    when (repository.initDefaultData()) {
                        is MainDataSource.NetworkResult.Success -> {
                            loadingViewStatePublisher.postValue(LoadingViewState.HideLoadingView)
                        }
                        is MainDataSource.NetworkResult.Error -> {

                        }
                    }
//                    repository.setUserData(Network.apiService.getDefault().await())
                    //repository.setStepGodData(Network.apiService.getStepGod().await())
                }
            }
        } catch (e: Exception) {
            Log.d("LaunchViewModel", e.message.toString())
        }
    }
}