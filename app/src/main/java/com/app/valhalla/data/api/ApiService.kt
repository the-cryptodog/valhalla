package com.app.valhalla.data.api

import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.RemoteByeResponse
import com.app.valhalla.data.model.StepBaseResult
import com.app.valhalla.data.model.StepContentBaseResult
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @POST("exec")
    fun getRemoteBye(@Body body:RequestBody): Call<RemoteByeResponse>

    @GET("exec?method=nextapi")
    fun getNextApi(): Call<BaseResult>

    @POST("exec")
    fun checkMember(@Body body:RequestBody): Call<BaseResult>

    @POST("exec")
    fun addMember(@Body body:RequestBody): Call<BaseResult>

    @GET("exec?method=getthings")
    fun getDefault(): Call<BaseResult>

    @GET("exec?method=gettodaystepgod")
    fun getStepGod(): Call<StepBaseResult>

    @GET("exec?method=getstepcontent")
    fun getStepContent(@Query("stepname")stepname:String,@Query("stepno")step_no:String): Call<StepContentBaseResult>

    @GET("things/{item}")
    fun getItems(@Path("item") uid: String): Call<BaseResult>

}