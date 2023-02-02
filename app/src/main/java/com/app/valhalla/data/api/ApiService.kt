package com.app.valhalla.data.api

import com.app.valhalla.data.model.BaseResult
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {

    @GET("defaultdesk.json")
    fun getDefault(): Call<BaseResult>
}