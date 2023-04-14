package com.app.valhalla.ui.main

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.valhalla.R
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.model.BgmManager
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.util.Constant

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {


    //    @Volatile
    private var loadingCount = 0

    private var currentSelectedType: String = ""

    private val _musicList = MutableLiveData<List<BgmManager.ByeMusic>>()
    val musicList : LiveData<List<BgmManager.ByeMusic>> = _musicList

    private val _objectSelectedEvent = MutableLiveData<String>()
    val objectSelectedEvent: LiveData<String> = _objectSelectedEvent

    //TODO 將MutableList修改的方式改為賦予新List
    private val _defaultGameObjList = MutableLiveData<List<GameObject>>()
    val defaultGameObjList: LiveData<List<GameObject>> = _defaultGameObjList

    private val _gameObjList = MutableLiveData<List<GameObject>>()
    val gameObjList: LiveData<List<GameObject>> = _gameObjList

    private val _dialogGameObj = MutableLiveData<List<GameObject>>()
    val dialogGameObj: LiveData<List<GameObject>> = _dialogGameObj

    private val _itemStepDataList = MutableLiveData<Parcelable>()
    val get_itemStepDataList: LiveData<Parcelable> = _itemStepDataList

    //MusicDialog 顯示狀態
    private val _mediaState = MutableLiveData<MediaState>()
    val mediaState: LiveData<MediaState> = _mediaState

    sealed class MediaState {
        object Playing : MediaState()
        object Pausing : MediaState()
        object ShowDialog : MediaState()
    }


    init {
        initAllItem()//must be set before initDefaultGameObj()
        initDefaultGameObj()
    }

    private fun initAllItem() {
        _gameObjList.value = repository.defaultData?.data?.toMutableList()
    }

    private fun initDefaultGameObj() {
        _defaultGameObjList.postValue(_gameObjList.value?.filter { it.is_default })
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
            it.id == itemId
        }.also {
            _objectSelectedEvent.value = it?.imgUrl()
        }
        _defaultGameObjList.value?.toMutableList()?.apply {
            set(index!!, obj!!)
            _defaultGameObjList.value = this
        }
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
        when (mediaState.value ) {
            is MediaState.Playing -> _mediaState.value = MediaState.Pausing
            is MediaState.Pausing -> _mediaState.value = MediaState.Playing
            else -> {
                _mediaState.value = MediaState.ShowDialog
            }
        }
    }

    fun setMusic(){
        _musicList.value = listOf<BgmManager.ByeMusic>(
            BgmManager.ByeMusic("歌曲3", R.raw.dao_music_1),
            BgmManager.ByeMusic("歌曲5", R.raw.freeloop),
            BgmManager.ByeMusic("歌曲6", R.raw.launch_music)
        )
    }
}
