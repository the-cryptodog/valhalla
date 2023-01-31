package com.app.valhalla.ui.drawlots

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.*
import com.app.valhalla.R
import com.app.valhalla.util.GifUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifDrawable
import kotlin.random.Random

class DrawLotsViewModel: ViewModel() {
    private val _saveStepShakeVisible = MutableLiveData<Boolean>()//籤筒判斷
    private val _saveDivinationBlocks = MutableLiveData<Boolean>()//籤判斷
    private val _saveStepThrowVisible = MutableLiveData<Boolean>()//筊判斷
    private val _saveStepAnswer = MutableLiveData<Int>()//筊結果
    private val _saveStepAnswerString = MutableLiveData<Int>()//筊結果文字
    val getStepShakeData: LiveData<Boolean> get() = _saveStepShakeVisible
    val getDivinationBlocks: LiveData<Boolean> get() = _saveDivinationBlocks
    val getStepThrowVisible: LiveData<Boolean> get() = _saveStepThrowVisible
    val getStepAnswer: LiveData<Int> get() = _saveStepAnswer
    val getStepAnswerString: LiveData<Int> get() = _saveStepAnswerString


    private var int_throw_positive_count:Int = 0

    fun StepOne(){
        /**
         * 第一步為顯示籤桶搖晃，抽出籤結果
         */
        f_subfunctionVisibleControl(true,false,false)
        viewModelScope.launch(Dispatchers.IO){
            delay(3*1000)
            withContext(Dispatchers.Main){
                f_subfunctionVisibleControl(false,true,false)
            }
        }
    }
    fun StepTwo(){
        /**
         * 第二步顯示擲筊，並出現結果
         */
        f_subfunctionVisibleControl(false,false,true)
        _saveStepAnswer.value = R.drawable.throw_blocks
        viewModelScope.launch(Dispatchers.IO){
            delay(1*1500)
            withContext(Dispatchers.Main){
                _saveStepAnswer.value = f_randomtogetAnswer()
                when(getStepAnswer.value){
                    R.drawable.throw_positive ->{
                        int_throw_positive_count++
                        _saveStepAnswerString.value = R.string.text_title_throw_positve
                    }
                    R.drawable.throw_negative ->{
                        int_throw_positive_count = 0
                        _saveStepAnswerString.value = R.string.text_title_throw_negative
                    }
                    R.drawable.throw_laugh ->{
                        int_throw_positive_count = 0
                        _saveStepAnswerString.value = R.string.text_title_throw_laugh
                    }
                }
            }
        }
    }

    fun StepThree(){
        /**
         * 判斷擲筊結果按鈕事件是要繼續擲筊還是重抽籤
         */
        when(getStepAnswer.value){
            R.drawable.throw_positive -> StepTwo()
            R.drawable.throw_negative -> StepOne()
            R.drawable.throw_laugh -> StepOne()
        }
    }

    fun f_subfunctionVisibleControl(is_StepShake:Boolean,is_Divination:Boolean,is_StepThrow:Boolean){
        _saveStepShakeVisible.value = is_StepShake
        _saveDivinationBlocks.value = is_Divination
        _saveStepThrowVisible.value = is_StepThrow
    }
    fun f_randomtogetAnswer(): Int {
        val list = listOf(0,1)
        val randomElement_left = list[Random.nextInt(list.size)]
        val randomElement_right = list[Random.nextInt(list.size)]
        if(randomElement_left == randomElement_right){
            if(randomElement_left == 1){
                return R.drawable.throw_laugh
            }else{
                return R.drawable.throw_negative
            }
        }else{
            return R.drawable.throw_positive
        }
    }
}