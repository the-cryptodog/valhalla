package com.app.valhalla.ui.stepcontent

import android.util.Log
import com.app.valhalla.data.api.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class StepContentRepository(private val listener:ReturnContentListener) {
    suspend fun get_Content(){
        try {
            var test=Network.apiService.getStepContent().await()
            Log.d("stepContent",test.message)
            withContext(Dispatchers.Main){
                listener.onStepContentCallback(test.message)
            }
        } catch (e: Exception) {
            Log.d("stepContent",  e.message.toString())
        }
    }

    interface ReturnContentListener{
        fun onStepContentCallback(str_content:String)
    }

}