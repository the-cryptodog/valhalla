package com.app.valhalla.ui.main

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.model.BaseUi
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.util.Constant
import com.app.valhalla.util.notifyObserver

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {


    //    @Volatile
    private var loadingCount = 0

    private var currentSelectedType: String = ""


    private val _objectSelectedEvent = MutableLiveData<String>()
    val objectSelectedEvent: LiveData<String> = _objectSelectedEvent

    //TODO 將MutableList修改的方式改為賦予新List
    private val _defaultGameObjList = MutableLiveData<MutableList<GameObject>>()
    val defaultGameObjList: LiveData<MutableList<GameObject>> = _defaultGameObjList

    private val _gameObjList = MutableLiveData<MutableList<GameObject>>()
    val gameObjList: LiveData<MutableList<GameObject>> = _gameObjList

    private val _btnFunction = MutableLiveData<MutableMap<String, BaseUi>>()
    val btnFunction: LiveData<MutableMap<String, BaseUi>> = _btnFunction

    private val _dialogGameObj = MutableLiveData<List<GameObject>>()
    val dialogGameObj: LiveData<List<GameObject>> = _dialogGameObj

    private val _itemDataList = MutableLiveData<MutableList<GameObject>>()
    val itemDataList: LiveData<MutableList<GameObject>> = _itemDataList

    private val _itemStepDataList = MutableLiveData<Parcelable>()
    val get_itemStepDataList: LiveData<Parcelable> = _itemStepDataList


    //itemDialog狀態
    private val _itemDialog = MutableLiveData<Int>()
    val itemDialog: LiveData<Int> = _itemDialog


    //MusicDialog 顯示狀態
    private val showMusicDialog = MutableLiveData(false)
    val _showMusicDialog: LiveData<Boolean> = showMusicDialog

    //MusicDialog 內部狀態
    private val isMusicPlaying = MutableLiveData(false)
    val _isMusicPlaying: LiveData<Boolean> = isMusicPlaying


    init {
//        fetchData()
        initFunctionButton()
        initAllItem()
        initDefaultGameObj()
    }

    private fun initAllItem() {
        _gameObjList.value = repository.defaultData?.data?.toMutableList()
    }

    private fun initDefaultGameObj() {
        val list = _gameObjList.value?.filter { it.is_default }
        _defaultGameObjList.postValue(list?.toMutableList())
    }

    //篩選同物件的list傳給dialog使用
    private fun openItemBag(itemType: String) {
        val list = gameObjList.value?.filter { it.type == itemType }
        _dialogGameObj.value = list?.toMutableList()
    }

    private fun objectSelected(itemType: String) {
        //defaultGameObjList 是一個固定長度的容器， 存放當前呈現在桌面的物件資料，一但有替換，及改變此值
        _defaultGameObjList.value?.find { it.type == itemType }.let {

            if (it != null) {
                //通知activity換圖
                _objectSelectedEvent.value = it.imgUrl()
                //當前選取的物件類別（）
                currentSelectedType = it.type

            }
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
        val obj = _dialogGameObj.value?.find {
            it.id == itemId }.also {
            _objectSelectedEvent.value = it?.imgUrl()
        }
        //更新預設設定組
        Log.d("TAGG", obj.toString())
        _defaultGameObjList.value?.set(index!!, obj!!)
        _defaultGameObjList.notifyObserver()
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

    fun addLoadingCount() {
        loadingCount++
        Log.d("TAGB", "loadingCountViewModel = $loadingCount")
        if (loadingCount == Constant.ALL_MAIN_ITEM_COUNT) {
            loadingViewStatePublisher.value = LoadingViewState.MainActivityImageLoadingDone
        }
    }


    fun toggleMusicDialog() {
        if (showMusicDialog.value == true) {
            showMusicDialog.postValue(false)
        } else {
            showMusicDialog.postValue(true)
        }
    }


}
