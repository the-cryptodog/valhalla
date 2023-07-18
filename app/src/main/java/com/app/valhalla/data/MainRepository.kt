package com.app.valhalla.data

import android.util.Log
import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.RemoteByeResponse
import com.app.valhalla.data.model.StepBaseResult
import com.app.valhalla.util.Constant

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class MainRepository(private val dataSource: MainDataSource) {

    // 初始化之所有桌面物件，玩家資料
    var defaultData: BaseResult? = null
        private set

    var stepGodData: StepBaseResult? = null
        private set


    suspend fun remoteBye(
        id: String,
        categoryId: String,
        thingsId: String
    ): MainDataSource.NetworkResult<RemoteByeResponse?> {
        return when (val result = dataSource.remoteBye(id, categoryId, thingsId)) {
            is MainDataSource.NetworkResult.Success -> {
                Log.d("FFF", "result=$result")
                MainDataSource.NetworkResult.Success(result.successObjectData)
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }

    suspend fun initDefaultData(): MainDataSource.NetworkResult<BaseResult?> {
        return when (val result = dataSource.initDefaultData()) {
            is MainDataSource.NetworkResult.Success -> {
                val data = result.successObjectData
                if (data != null) {
                    setDefaultData(data)
                }
                MainDataSource.NetworkResult.Success(data)
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }

    suspend fun getNextApi(): MainDataSource.NetworkResult<BaseResult?> {
        return when (val result = dataSource.getNextApi()) {
            is MainDataSource.NetworkResult.Success -> {
                MainDataSource.NetworkResult.Success(result.successObjectData)
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }

    suspend fun checkMember(uId: String, email: String?, pwd:String?): MainDataSource.NetworkResult<BaseResult?> {
        Log.d("FFF", "email＝$email pwd＝$pwd  id＝$uId" )
        return when (val result = dataSource.checkMember(uId,email,pwd)) {
            is MainDataSource.NetworkResult.Success -> {
                val data = result.successObjectData
                if (data != null) {
                    setDefaultData(data)
                }
                MainDataSource.NetworkResult.Success(result.successObjectData)
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }


    suspend fun addMember(
        uId: String,
        email: String,
        nickname: String,
        pwd:String
    ): MainDataSource.NetworkResult<BaseResult?> {
        return when (val result = dataSource.addMember(uId, email, nickname,pwd)) {
            is MainDataSource.NetworkResult.Success -> {
                Log.d("FFF", "result=$result.data")
                MainDataSource.NetworkResult.Success(result.successObjectData)
                //TODO 新增主委後的流程
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }

    suspend fun login() {

    }

    suspend fun getStepGod(): MainDataSource.NetworkResult<StepBaseResult?>{
        return when (val result = dataSource.getStepGod()) {
            is MainDataSource.NetworkResult.Success -> {
                Log.d("FFF", "Today Step God result=$result.data")
                MainDataSource.NetworkResult.Success(result.successObjectData)
                //TODO 求籤頁取得當日神明
            }
            is MainDataSource.NetworkResult.Error -> {
                val exception = result.exception
                MainDataSource.NetworkResult.Error(exception)
            }
        }
    }



    private fun setDefaultData(defaultData: BaseResult) {
        this.defaultData = defaultData
    }

    fun setStepGodData(stepGodData: StepBaseResult?) {
        this.stepGodData = stepGodData
    }

    fun getDataSource(): MainDataSource {
        return dataSource
    }
}