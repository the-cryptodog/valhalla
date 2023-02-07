package com.app.valhalla.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.BaseUi
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.util.Constant
import com.app.valhalla.util.notifyObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class MainViewModel : ViewModel() {


    private var currentSelectedId: String = ""

    private var currentSelectedType: String = ""

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

    //itemDialog狀態
    private val _itemDialog = MutableLiveData<Int>()
    val itemDialog: LiveData<Int> = _itemDialog

    init {
        fetchData()
        initFunctionButton()
    }


    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val call = Network.apiService.getDefault().await()
                Log.d("TAG", call.toString())
                //以 type為 key 建立map
                val r = call.data.toMutableList()
                withContext(Dispatchers.Main) {
                    _gameObjList.value = r
                    initDefaultGameObj()
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
        Log.d("TAGG",obj.toString())
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
            current.imgUrl = obj?.img_url.toString()
            _btnFunction.value?.put(Constant.BUTTON_LEFT, current)
            _btnFunction.notifyObserver()
        }
    }


    fun initFunctionButton() {
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
