package com.app.valhalla.data.api

import android.util.Base64
import com.app.valhalla.util.Constant
import com.blankj.utilcode.util.DeviceUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

object Network {
    val apiService: ApiService
        get() = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initClient())
            .build()
            .create(ApiService::class.java)

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY) // 設定NO_PROXY，防止被攔截封包
            .addInterceptor(initLogInterceptor())
            .addInterceptor(getInterceptor())
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    private fun getInterceptor(): Interceptor {
        val interceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content_Type", "application/json")
                .build()
            chain.proceed(request)
        }
        return interceptor
    }
    /*
        日誌攔截器
     */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
    }
}