package com.app.valhalla.ui.stepcontent

import android.util.Log
import com.app.valhalla.data.api.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class StepContentRepository(private val listener:ReturnContentListener) {
    suspend fun get_Content(step_name:String,step_no:String){
        try {
            var stepcontentquery=Network.apiService.getStepContent(step_name,step_no).await()
            Log.d("stepContent",stepcontentquery.message)
            withContext(Dispatchers.Main){
                listener.onStepContentCallback(stepcontentquery.message)
            }
        } catch (e: Exception) {
            Log.d("stepContent",  e.message.toString())
        }
    }

    interface ReturnContentListener{
        fun onStepContentCallback(str_content:String)
    }

}