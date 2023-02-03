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


    private val _gameObjHashMap = MutableLiveData<MutableMap<String, GameObject>>()
    val gameObjHashMap: LiveData<MutableMap<String, GameObject>> = _gameObjHashMap

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
                val r = call.data.associateBy { it.id }
                withContext(Dispatchers.Main) {
                    _gameObjHashMap.value = r.toMutableMap()
                }
            }
        }
    }

    private fun objectSelected(objectId: String) {
        //檢查map是否有該view
        if (_gameObjHashMap.value?.contains(objectId) == true) {
            val selectedGameObject: GameObject? = _gameObjHashMap.value?.get(objectId)
            if (selectedGameObject != null) {
                selectedGameObject.isSelected = true
                _gameObjHashMap.value?.put(objectId, selectedGameObject)
                //修改底部按鈕資料
                val current = _btnFunction.value?.get(Constant.BUTTON_LEFT)
                if (current != null) {
                    current.imgUrl = selectedGameObject.img_url
                    _btnFunction.value?.put(Constant.BUTTON_LEFT, current)
                    _btnFunction.notifyObserver()
                }
            }
        }
    }

    fun leftFunctionLaunch() {
        _dialogGameObj.value = mutableListOf(GameObject())
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