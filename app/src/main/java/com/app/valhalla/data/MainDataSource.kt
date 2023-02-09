package com.app.valhalla.data

import androidx.lifecycle.Lifecycle
import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.LoggedInUser
import kotlinx.coroutines.coroutineScope
import retrofit2.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class MainDataSource{

    suspend fun fetchData(token: String): Result<BaseResult> {
        try {
            val data = Network.apiService.getDefault().await()
            return Result.Success(data)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error data", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}