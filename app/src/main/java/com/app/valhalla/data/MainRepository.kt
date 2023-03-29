package com.app.valhalla.data

import com.app.valhalla.data.model.BaseResult
import com.app.valhalla.data.model.StepBaseResult

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


    suspend fun initDefaultData(): MainDataSource.NetworkResult<BaseResult?> {
        return when (val result = dataSource.initDefaultData()) {
            is MainDataSource.NetworkResult.Success -> {
                val data = result.data
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


    private fun setDefaultData(defaultData: BaseResult) {
        this.defaultData = defaultData
    }

    fun setStepGodData(stepGodData: StepBaseResult) {
        this.stepGodData = stepGodData
    }

    fun getDataSource(): MainDataSource{
        return dataSource
    }
}