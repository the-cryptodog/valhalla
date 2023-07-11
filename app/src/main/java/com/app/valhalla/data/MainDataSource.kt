package com.app.valhalla.data

import android.util.Log
import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.util.Constant
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import java.io.IOException
import kotlin.random.Random

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class MainDataSource {

    companion object {
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    suspend fun getNextApi(): NetworkResult<BaseResult?> {
        val response = Network.apiService.getNextApi().awaitResponse()
        return if (response.isSuccessful) {
            NetworkResult.Success(response.body())
        } else {
            NetworkResult.Error(response.message())
        }
    }

    suspend fun checkMember(
        uId: String,
        mail: String?,
        pwd: String?
    ): NetworkResult<BaseResult?> {
        val body = JSONObject().apply {
            put("method", "checkmember")
            put("deviceid", uId)
            put("email", mail ?: "")
            put("pwd", pwd ?: "")
        }
        Log.d("FFFK",body.toString())
        val response = Network.apiService.checkMember(  body. toString().toRequestBody(JSON_MEDIA_TYPE)).awaitResponse()
        return if (isRequestSuccessful(response)) {
            NetworkResult.Success(response.body())
        } else {
            NetworkResult.Error(response.body().toString())
        }
    }


    suspend fun addMember(
        uId: String,
        email: String,
        nickname: String,
        pwd:String
    ): NetworkResult<BaseResult?> {
        val body = JSONObject().apply {
            put("method", "addmember")
            put("deviceid", uId)
            put("pwd", pwd)
            put("email", email)
            put("nickname", nickname)

        }.toString().toRequestBody(JSON_MEDIA_TYPE)
        val response = Network.apiService.addMember(body).awaitResponse()
        return if (isRequestSuccessful(response)) {
            NetworkResult.Success(response.body())
        } else {
            NetworkResult.Error(response.body().toString())
        }
    }


    suspend fun initDefaultData(): NetworkResult<BaseResult?> {
        val response = Network.apiService.getDefault().awaitResponse()
        return if (isRequestSuccessful(response)) {
            NetworkResult.Success(response.body())
        } else {
            NetworkResult.Error(response.body().toString())
        }
    }

    sealed class NetworkResult<out T> {
        data class Success<out T>(val data: T) : NetworkResult<T>()
        data class Error(val exception: String) : NetworkResult<Nothing>()
    }

    private fun isRequestSuccessful(response:Response<BaseResult>) :Boolean{
        return (response.isSuccessful && (response.body()?.result == "200"))
    }

}