package com.app.valhalla.data

import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.GameObject
import retrofit2.await
import retrofit2.awaitResponse
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class MainDataSource{

    suspend fun initDefaultData(): NetworkResult<BaseResult?> {
        val response = Network.apiService.getDefault().awaitResponse()
        return if(response.isSuccessful){
            NetworkResult.Success(response.body())
        }else{
            NetworkResult.Error(response.message())
        }
    }

    sealed class NetworkResult<out T> {
        data class Success<out T>(val data: T) : NetworkResult<T>()
        data class Error(val exception: String) : NetworkResult<Nothing>()
    }

}