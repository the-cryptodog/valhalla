package com.app.valhalla.data.api

import com.app.valhalla.util.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    val apiService: ApiService
        get() = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}