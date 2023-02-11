package com.app.valhalla.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.Result
import com.app.valhalla.data.model.*
import com.app.valhalla.util.Constant
import com.app.valhalla.util.notifyObserver
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val repository: MainRepository by lazy {
        MainRepository(MainDataSource())
    }

    private var currentSelectedId: String = ""

    private var currentSelectedType: String = ""

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinishedFlow: LiveData<Boolean> = _isLoadingFinished


    private val _defaultGameObjList = MutableLiveData<MutableList<GameObject>>()
    val defaultGameObjList: LiveData<MutableList<GameObject>> = _defaultGameObjList

    private val _gameObjList = MutableLiveData<MutableList<GameObject>>()
    val gameObjList: LiveData<MutableList<GameObject>> = _gameObjList

    private val _btnFunction = MutableLiveData<MutableMap<String, BaseUi>>()
    val btnFunction: LiveData<MutableMap<String, BaseUi>> = _btnFunction

    private val _dialogGameObj = MutableLiveData<MutableList<GameObject>>()
    val dialogGameObj: LiveData<MutableList<GameObject>> = _dialogGameObj

    private val _itemDataList = MutableLiveData<MutableList<GameObject>>()
    val itemDataList: LiveData<MutableList<GameObject>> = _itemDataList

    private val _itemStepDataList = MutableLiveData<Parcelable>()
    val get_itemStepDataList: LiveData<Parcelable> = _itemStepDataList

    //itemDialog狀態
    private val _itemDialog = MutableLiveData<Int>()
    val itemDialog: LiveData<Int> = _itemDialog

    init {
//        fetchData()
        initFunctionButton()
    }

    fun loadData(bundle: Bundle) {
        val data: BaseResult? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable("data", BaseResult::class.java)
        } else {
            bundle.getParcelable("data")
        }
        _itemStepDataList.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable("stepGodData", StepBaseResult::class.java)
        } else {
            bundle.getParcelable("stepGodData")
        }
        Log.d("TAGB", "loadData: $data")
        Log.d("Goddata", "$get_itemStepDataList")
        repository.setUserData(data!!)
        initAllItem()
        initDefaultGameObj()
    }

    private fun initAllItem() {
        _gameObjList.value = repository.userData?.data?.toMutableList()
    }

    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = repository.fetchData()
                if (result is Result.Success) {
                    withContext(Dispatchers.Main) {
                        _gameObjList.value = result.baseResult.data.toMutableList()
                        initDefaultGameObj()
                    }
                } else {
                    ToastUtils.showShort(R.string.login_failed)
                }
            }
        }
    }

    private fun initDefaultGameObj() {

        val list = _gameObjList.value?.filter { it.is_default }
        _defaultGameObjList.postValue(list?.toMutableList())
    }

    private fun openItemBag(itemType: String) {
        val list = _gameObjList.value?.filter { it.type == itemType }
        _dialogGameObj.postValue(list?.toMutableList())
    }

    private fun objectSelected(itemType: String) {
        //從類別與預設值去搜尋
        _defaultGameObjList.value?.find { it.type == itemType && it.is_default }.let {
            Log.d("TAGG", it?.img_url.toString())


            if (it != null) {
                updateFunctionIcon(it)
            }

            //暫存選定物件之id與type
            currentSelectedId = it?.id.toString()
            currentSelectedType = it?.type.toString()
        }
    }

    fun leftFunctionLaunch() {
        //先從預設設定組中尋找對應類別物件的id
        openItemBag(currentSelectedType)
    }

    fun updateCurrentSelectedItem(itemType: String, itemId: String) {
        //先從預設設定組中尋找對應類別物件的index
        val index = _defaultGameObjList.value?.indexOfFirst { it.type == itemType }
        //先從dialog list物件中找到該id物件
        val obj = _dialogGameObj.value?.find { it.id == itemId }
        //更新預設設定組
        Log.d("TAGG", obj.toString())
        _defaultGameObjList.value?.set(index!!, obj!!)
        _defaultGameObjList.notifyObserver()

        if (obj != null) {
            updateFunctionIcon(obj)
        }
    }

    //更新功能圖icon
    private fun updateFunctionIcon(obj: GameObject) {
        val current = _btnFunction.value?.get(Constant.BUTTON_LEFT)
        if (current != null) {
            current.imgUrl = obj.imgUrl()
            _btnFunction.value?.put(Constant.BUTTON_LEFT, current)
            _btnFunction.notifyObserver()
        }
    }


    private fun initFunctionButton() {
        _btnFunction.value = mutableMapOf(
            Constant.BUTTON_LEFT to BaseUi(Constant.BUTTON_LEFT),
            Constant.BUTTON_RIGHT to BaseUi(Constant.BUTTON_RIGHT),
            Constant.BUTTON_MID to BaseUi(Constant.BUTTON_MID),
        )
    }


    fun vaseSelected() {
        objectSelected(Constant.OBJ_VASE)
    }

    fun incenseSelected() {
        objectSelected(Constant.OBJ_INCENSE_ID)
    }

    fun incenseBurnerSelected() {
        objectSelected(Constant.OBJ_INCENSE_BURNER_ID)
    }

    fun tableSelected() {
        objectSelected(Constant.OBJ_TABLE)
    }

    fun jossBackgroundSelected() {
        objectSelected(Constant.OBJ_JOSS_BACKGROUND_ID)
    }

    fun jossSelected() {
        objectSelected(Constant.OBJ_JOSS)
    }

    fun candleSelected() {
        objectSelected(Constant.OBJ_CANDLE_ID)
    }

    fun flowerSelected() {
        objectSelected(Constant.OBJ_FLOWER_ID)
    }

}
