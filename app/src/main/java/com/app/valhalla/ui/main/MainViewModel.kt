package com.app.valhalla.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.valhalla.R
import com.app.valhalla.data.api.Network
import com.app.valhalla.data.model.GameObject
import com.app.valhalla.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class MainViewModel : ViewModel() {


    private val _hasInitData = MutableLiveData<Boolean>()
    val hasInitData: LiveData<Boolean> = _hasInitData
    private val _btnFucntionOne = MutableLiveData<Int>()
    val btnFucntionOne: LiveData<Int> = _btnFucntionOne
    private val _btnFunctionTwo = MutableLiveData<Int>()
    val btnFunctionTwo: LiveData<Int> = _btnFunctionTwo
    private val _btnFunctionThree = MutableLiveData<Int>()
    val btnFunctionThree: LiveData<Int> = _btnFunctionThree
    private val _imgIncense = MutableLiveData<Int>()
    val imgIncense: LiveData<Int> = _imgIncense
    private val _imgIncenseBurner = MutableLiveData<Int>()
    val imgIncenseBurner: LiveData<Int> = _imgIncenseBurner
    private val _imgCandleRight = MutableLiveData<Int>()
    val imgCandleRight: LiveData<Int> = _imgCandleRight
    private val _imgCandleLeft = MutableLiveData<Int>()
    val imgCandleLeft: LiveData<Int> = _imgCandleLeft
    private val _imgFlowerLeft = MutableLiveData<Int>()
    val imgFlowerLeft: LiveData<Int> = _imgFlowerLeft
    private val _imgFlowerRight = MutableLiveData<Int>()
    val imgFlowerRight: LiveData<Int> = _imgFlowerRight
    private val _imgLeftCouplet = MutableLiveData<Int>()
    val imgLeftCouplet: LiveData<Int> = _imgLeftCouplet
    private val _imgRightCouplet = MutableLiveData<Int>()
    val imgRightCouplet: LiveData<Int> = _imgRightCouplet
    private val _imgUpperCouplet = MutableLiveData<Int>()
    val imgUpperCouplet: LiveData<Int> = _imgUpperCouplet
    private val _imgJoss = MutableLiveData<Int>()
    val imgJoss: LiveData<Int> = _imgJoss
    private val _imgJossBackground = MutableLiveData<Int>()
    val imgJossBackground: LiveData<Int> = _imgJossBackground
    private val _imgVaseLeft = MutableLiveData<Int>()
    val imgVaseLeft: LiveData<Int> = _imgVaseLeft
    private val _imgVaseRight = MutableLiveData<Int>()
    val imgVaseRight: LiveData<Int> = _imgVaseRight
    private val _imgTable = MutableLiveData<Int>()
    val imgTable: LiveData<Int> = _imgTable

    //功能按鈕切換
    private val _btnFucntionOneImgId = MutableLiveData<Int>()
    var btnFucntionOneImgId: LiveData<Int> = _btnFucntionOneImgId
    private val _btnFucntionOTwoImgId = MutableLiveData<Int>()
    val btnFucntionOTwoImgId: LiveData<Int> = _btnFucntionOTwoImgId
    private val _btnFucntionThreeImgId = MutableLiveData<Int>()
    val btnFucntionThreeImgId: LiveData<Int> = _btnFucntionThreeImgId

    //api回傳資料
    private var currentItem: String = ""

    private val _itemDataList = MutableLiveData<List<GameObject>>()
    val itemDataList: LiveData<List<GameObject>> = _itemDataList

    //itemDialog狀態
    private val _itemDialog = MutableLiveData<Int>()
    val itemDialog: LiveData<Int> = _itemDialog


    //Constant.VIEW_OPEN.點擊物件  點擊：Constant.VIEW_OPEN
    fun itemStatus(viewEntryName: String) {
        Log.d("TAG", viewEntryName)

        //2.改變物件對應LiveData狀態
        when (viewEntryName) {
            "btn_function_one" -> {
                if (currentItem.isNotBlank()) {
                    _itemDialog.postValue(Constant.VIEW_OPEN)
                }
                _btnFucntionOne.postValue(Constant.VIEW_OPEN)
            }
            "btn_function_two" -> {
                Log.d("TAG", "btn22222")



//                val call = Network.apiService.test().enqueue(object : Callback<List<TestData>> {
//                    override fun onResponse(
//                        call: Call<List<TestData>>,
//                        response: Response<List<TestData>>
//                    ) {
//                        Log.d("TAG",response.body()!!.get(0).title.toString())
//                    }
//
//                    override fun onFailure(call: Call<List<TestData>>, t: Throwable) {
//                    }
//                })
                currentItem = "btn_function_two"
                _btnFunctionTwo.postValue(Constant.VIEW_OPEN)
            }
            "btn_function_three" -> {
                currentItem = "btn_function_three"
                _btnFunctionThree.postValue(Constant.VIEW_OPEN)
            }
            "img_incense" -> {
                currentItem = "img_incense"
                _imgIncense.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.incence)
            }
            "img_incense_burner" -> {
                currentItem = "img_incense_burner"
                _imgIncenseBurner.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.insenceburner)
            }
            "img_candle_right" -> {
                currentItem = "img_candle_right"
                _imgCandleRight.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.candle)
            }
            "img_candle_left" -> {
                currentItem = "img_candle_left"
                _imgCandleLeft.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.candle)
            }
            "img_flower_left" -> {
                currentItem = "img_flower_left"
                _imgFlowerLeft.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.flowers)
            }
            "img_flower_right" -> {
                currentItem = "img_flower_right"
                _imgFlowerRight.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.flowers)
            }
            "img_left_couplet" -> {
                currentItem = "img_left_couplet"
                _imgLeftCouplet.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.couplet)
            }
            "img_right_couplet" -> {
                currentItem = "img_right_couplet"
                _imgRightCouplet.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.couplet)
            }
            "img_upper_couplet" -> {
                currentItem = "img_upper_cuplet"
                _imgUpperCouplet.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.uppercouplet)
            }
            "img_joss" -> {
                currentItem = "img_joss"
                _imgJoss.postValue(Constant.VIEW_OPEN)
            }
            "img_JossBackground" -> {
                currentItem = "img_joss_background"
                _imgJossBackground.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.jossbackground)
            }
            "img_vaＯse_left" -> {
                currentItem = "img_vase_left"
                _imgVaseLeft.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.vase)
            }
            "img_vase_right" -> {
                currentItem = "img_vase_right"
                _imgVaseRight.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.vase)
            }
            "img_table" -> {
                //神明桌
                currentItem = "img_table"
                _imgTable.postValue(Constant.VIEW_OPEN)
                toggleFirstFunctionImg(R.drawable.table)
            }
        }
    }

    //3.切換底層圖樣
    private fun toggleFirstFunctionImg(itemSelectedId: Int) {
        _btnFucntionOneImgId.postValue(itemSelectedId)
    }
}