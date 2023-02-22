package com.app.valhalla.data.api

import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.StepBaseResult
import com.app.valhalla.data.model.StepContentBaseResult
import com.app.valhalla.data.model.StepContentObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("things/default")
    fun getDefault(): Call<BaseResult>

    @GET("api_getstepgod.php")
    fun getStepGod(): Call<StepBaseResult>

    @GET("api_getstep.php")
    fun getStepContent(): Call<StepContentBaseResult>


    @GET("things/{item}")
    fun getItems(@Path("item") uid: String): Call<BaseResult>

}