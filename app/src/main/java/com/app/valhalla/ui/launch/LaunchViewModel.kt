package com.app.valhalla.ui.launch

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.api.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class LaunchViewModel(private val repository: MainRepository) : BaseViewModel() {

    init {
        //initData()
    }

    fun genSound(context: Context) {
        MediaPlayer.create(context, R.raw.launch_music)?.start()
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