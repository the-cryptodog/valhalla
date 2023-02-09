package com.app.valhalla.data

import android.os.Bundle
import com.app.valhalla.data.model.BaseResult

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class MainRepository(private val dataSource: MainDataSource) {

    // in-memory cache of the loggedInUser object
    var userData: BaseResult? = null
        private set

    val isLoggedIn: Boolean
        get() = userData != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        userData = null
    }

    suspend fun fetchData(): Result<BaseResult> {

        val result = dataSource.fetchData("")

        if (result is Result.Success) {
            setUserData(result.baseResult)
        }
        return result
    }


    fun setUserData(userData: BaseResult) {
        this.userData = userData
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}