package com.app.valhalla.ui.main.dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.valhalla.R

class MainViewModel : ViewModel() {


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


    private val _btnFucntionOneImgId = MutableLiveData<Int>()
    var btnFucntionOneImgId: LiveData<Int> = _btnFucntionOneImgId
    private val _btnFucntionOTwoImgId = MutableLiveData<Int>()
    val btnFucntionOTwoImgId: LiveData<Int> = _btnFucntionOTwoImgId
    private val _btnFucntionThreeImgId = MutableLiveData<Int>()
    val btnFucntionThreeImgId: LiveData<Int> = _btnFucntionThreeImgId


    fun setCurrentItemId(itemId: Int) {

    }

    //1.點擊物件  點擊：1
    fun itemStatus(viewEntryName: String) {
        Log.d("TAG",viewEntryName)
        //2.改變物件對應LiveData狀態
        when (viewEntryName) {
            "btn_function_one" -> {
                _btnFucntionOne.postValue(1)

            }
            "btn_function_two" -> {
                _btnFunctionTwo.postValue(1)
            }
            "btn_function_three" -> {
                _btnFunctionThree.postValue(1)
            }
            "img_incense" -> {
                _imgIncense.postValue(1)
            }
            "img_incense_burner" -> {
                _imgIncenseBurner.postValue(1)
            }
            "img_candle_right" -> {
                _imgCandleRight.postValue(1)
            }
            "img_candle_left" -> {
                _imgCandleLeft.postValue(1)
            }
            "img_flower_left" -> {
                _imgFlowerLeft.postValue(1)
            }
            "img_flower_right" -> {
                _imgFlowerRight.postValue(1)
            }
            "img_left_couplet" -> {
                _imgLeftCouplet.postValue(1)
            }
            "img_right_couplet" -> {
                _imgRightCouplet.postValue(1)
            }
            "img_upper_cuplet" -> {
                _imgUpperCouplet.postValue(1)
            }
            "img_joss" -> {
                _imgJoss.postValue(1)
            }
            "img_joss_background" -> {
                _imgJossBackground.postValue(1)
            }
            "img_vase_left" -> {
                _imgVaseLeft.postValue(1)
            }
            "img_vase_right" -> {
                _imgVaseRight.postValue(1)
            }
            "img_table" -> {
                //神明桌
                _imgTable.postValue(1)
                toggleFirstFunctionImg(R.drawable.icon_table)
            }
        }
    }

    //3.切換底層圖樣
    private fun toggleFirstFunctionImg(itemSelectedId:Int) {
        _btnFucntionOneImgId.postValue(itemSelectedId)
    }
}