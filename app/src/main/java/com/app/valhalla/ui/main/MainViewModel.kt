package com.app.valhalla.ui.main

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.BaseViewModel
import com.app.valhalla.data.MainDataSource
import com.app.valhalla.data.MainRepository
import com.app.valhalla.data.model.*
import com.app.valhalla.util.Constant
import com.app.valhalla.util.GlobalUID
import com.app.valhalla.util.TimerManager
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {


    //    @Volatile
    private var loadingCount = 0

    private var isSwitchingModeOn: Boolean = false

    private var currentSelectedType: String = ""

    //拜拜 倒數時間顯示狀態
    val countdownTime: LiveData<String> = TimerManager.countdownTime

    val isCounting: LiveData<Boolean>  = TimerManager.isCounting

    sealed class MainItemViewState {
        data class ShowSwitchDialog(
            val data: List<GameObject>
        ) : MainItemViewState()

        object CloseSwitchDialog: MainItemViewState()
    }


    private val _mainItemViewState = MutableLiveData<MainItemViewState>()
    val mainItemViewState: LiveData<MainItemViewState> = _mainItemViewState


    private val _musicList = MutableLiveData<List<BgmManager.ByeMusic>>()
    val musicList: LiveData<List<BgmManager.ByeMusic>> = _musicList

    private val _gameObjList = MutableLiveData<List<GameObjects>>()
    val gameObjList: LiveData<List<GameObjects>> = _gameObjList

    private val _objectSelectedEvent = MutableLiveData<String>()
    val objectSelectedEvent: LiveData<String> = _objectSelectedEvent

    //TODO 將MutableList修改的方式改為賦予新List
    private val _defaultGameObjList = MutableLiveData<List<GameObject>>()
    val defaultGameObjList: LiveData<List<GameObject>> = _defaultGameObjList

    private val _dialogGameObjList = MutableLiveData<List<GameObject>>()
    val dialogGameObjList: LiveData<List<GameObject>> = _dialogGameObjList

    private val _itemStepDataList = MutableLiveData<Parcelable>()
    val get_itemStepDataList: LiveData<Parcelable> = _itemStepDataList

    private val _stepGodObjList = MutableLiveData<List<StepBaseResult>>()
    val get_stepGodObjList: LiveData<List<StepBaseResult>> = _stepGodObjList

    //MusicDialog 顯示狀態
    private val _mediaState = MutableLiveData<MediaState>()
    val mediaState: LiveData<MediaState> = _mediaState

    //Morality
    private val _morality = MutableLiveData<List<MoralityItem>>()
    val morality: LiveData<List<MoralityItem>> = _morality


    sealed class MediaState {
        object Playing : MediaState()
        object Pausing : MediaState()
        object ShowDialog : MediaState()
    }


    init {
        initAllItem()//must be set before initDefaultGameObj()
        initDefaultGameObj()
        initMorality()
        initTodayStepGod()
    }

    private fun initAllItem() {
        Log.d("FFF", repository.defaultData?.toString().toString())
        _gameObjList.value = repository.defaultData?.data?.toMutableList()
    }

    private fun initDefaultGameObj() {
        val defaultList: List<GameObject> =
            _gameObjList.value?.flatMap { it.things_data }!!.filter {
                it.is_default
            }
        _defaultGameObjList.value = defaultList
    }

    private fun initMorality() {
        if(repository.defaultData?.morality?.isEmpty() == true){
            Log.d("FFF", "不用倒數")
            TimerManager.isCounting.postValue(false)
            TimerManager.countdownTime.postValue("可以拜拜啦")
        }else{
            if(repository.defaultData?.morality!!.isNotEmpty()){
                Log.d("FFF", "要倒數+ ${repository.defaultData?.morality!![0].deadDatetime}")
                TimerManager.startCountDown(repository.defaultData?.morality!![0].deadDatetime)
            }
        }
    }

    private fun initTodayStepGod(){
        Log.d("FFF", repository.stepGodData?.toString().toString())
        _gameObjList.value = repository.stepGodData?.data?.toMutableList()
    }

    fun setDefaultGameObj(list:List<GameObject>) {
        _defaultGameObjList.value = list
    }

    //篩選同物件的list傳給dialog使用
    fun getTypeList(itemType: String) : List<GameObject> {
        //map{}每個元素轉換成為新元素，取代原集合，大小一樣，flatMap{}用原集合的元素，去組成一個新集合，操
        val list = _gameObjList.value?.filter { it.category_id == itemType }!!.flatMap { it.things_data }
        _dialogGameObjList.value = list
        return list
    }


    fun showSwitchDialog(itemType: String = "") {
        //先從預設設定組中尋找對應類別物件的id
        val tempItemType : String = if(itemType ==""){
            Constant.OBJ_JOSS
        }else{
            itemType
        }
        _mainItemViewState.value = MainItemViewState.ShowSwitchDialog(getTypeList(tempItemType))
    }

    fun updateCurrentSelectedItem(itemType: String, itemId: String) {
        //先從預設設定組中尋找對應類別物件的index
//        val index = _defaultGameObjList.value?.indexOfFirst { it.type == itemType }
//        //先從dialog list物件中找到該id物件
//        val obj = _dialogGameObj.value?.find {
//            it.id == itemId
//        }.also {
//            _objectSelectedEvent.value = it?.imgUrl()
//        }
//        _defaultGameObjList.value?.toMutableList()?.apply {
//            set(index!!, obj!!)
//            _defaultGameObjList.value = this
//        }
    }


    fun addLoadingCount() {
        loadingCount++
        Log.d("TAGB", "loadingCountViewModel = $loadingCount")
        if (loadingCount == Constant.ALL_MAIN_ITEM_COUNT) {
            loadingViewStatePublisher.value = LoadingViewState.MainActivityImageLoadingDone
        }
    }


    fun toggleMusicDialog() {
        when (mediaState.value) {
            is MediaState.Playing -> _mediaState.value = MediaState.Pausing
            is MediaState.Pausing -> _mediaState.value = MediaState.Playing
            else -> {
                _mediaState.value = MediaState.ShowDialog
            }
        }
    }

    fun setMusic() {
        _musicList.value = listOf<BgmManager.ByeMusic>(
            BgmManager.ByeMusic("歌曲3", R.raw.dao_music_1),
            BgmManager.ByeMusic("歌曲5", R.raw.freeloop),
            BgmManager.ByeMusic("歌曲6", R.raw.launch_music)
        )
    }

    fun remoteBye() {
        viewModelScope.launch {
            Log.d("FFF", "GlobalUID.getSavedUID() = "+GlobalUID.getSavedUID())
            withContext(Dispatchers.IO){
                val result = repository.remoteBye(GlobalUID.getSavedUID(),"l","l00")
                withContext(Dispatchers.Main) {
                    when(result){
                        is MainDataSource.NetworkResult.Success->{
                            if(result.successObjectData?.morality?.isNotEmpty() == true){
                                startCountDown(result.successObjectData.morality[0].deadDatetime)
                            }else{
                                //這裡只有登入時會判斷
                                TimerManager.countdownTime.postValue("可以拜拜了")
                            }
                        }
                        is MainDataSource.NetworkResult.Error -> {
                            ToastUtils.showLong("資料錯誤")
                        }
                    }
                }
            }
        }
    }


    private fun startCountDown(inputDateTime: String?) {
        TimerManager.startCountDown(inputDateTime)
    }
}
