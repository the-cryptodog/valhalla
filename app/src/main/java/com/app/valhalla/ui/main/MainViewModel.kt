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


    private var currentSelectedType :String = ""

    private val _gameObjList = MutableLiveData<List<GameObject>>()
    val gameObjList: LiveData<List<GameObject>> = _gameObjList

    private val _btnFunction = MutableLiveData<MutableMap<String, BaseUi>>()
    val btnFunction: LiveData<MutableMap<String, BaseUi>> = _btnFunction

    private val _dialogGameObj = MutableLiveData<List<GameObject>>()
    val dialogGameObj: LiveData<List<GameObject>> = _dialogGameObj

    private val _itemDataList = MutableLiveData<List<GameObject>>()
    val itemDataList: LiveData<List<GameObject>> = _itemDataList

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
                val r = call.data
                withContext(Dispatchers.Main) {
                    _gameObjList.value = r
                }
            }
        }
    }

    private fun openItemBag(item: String) {
        val list = _gameObjList.value?.filter { it.type == item}
        Log.d("TAGG", list.toString())
        _dialogGameObj.postValue(list!!)
    }

    private fun objectSelected(objectType: String) {
        //檢查map是否有該view
        _gameObjList.value?.find { it.type == objectType && it.is_default }.let {
            val current = _btnFunction.value?.get(Constant.BUTTON_LEFT)
            if (current != null) {
                current.imgUrl = it?.img_url.toString()
                _btnFunction.value?.put(Constant.BUTTON_LEFT, current)
                _btnFunction.notifyObserver()
            }
            currentSelectedType = it?.type.toString()
        }
    }

    fun leftFunctionLaunch() {
        openItemBag(currentSelectedType)
    }

    fun rightFunctionLaunch() {

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