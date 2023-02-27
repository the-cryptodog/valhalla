package com.app.valhalla.data.api

import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.StepBaseResult
import com.app.valhalla.data.model.StepContentBaseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("exec?method=getthings")
    fun getDefault(): Call<BaseResult>

    @GET("exec?method=gettodaystepgod")
    fun getStepGod(): Call<StepBaseResult>

    @GET("exec?method=getstepcontent")
    fun getStepContent(@Query("stepname")stepname:String,@Query("stepno")step_no:String): Call<StepContentBaseResult>


    @GET("things/{item}")
    fun getItems(@Path("item") uid: String): Call<BaseResult>

}