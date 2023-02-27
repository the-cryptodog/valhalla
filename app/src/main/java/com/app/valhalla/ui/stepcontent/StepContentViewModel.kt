package com.app.valhalla.ui.stepcontent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StepContentViewModel : ViewModel(),StepContentRepository.ReturnContentListener {
    private val repository= StepContentRepository(this)
    private val _saveStepContentData = MutableLiveData<String>()
    private val _saveIsImgCanVisible = MutableLiveData<Boolean>()
    private val _saveIsContentCanVisible = MutableLiveData<Boolean>()
    val getStepContentData: LiveData<String> get() = _saveStepContentData
    val getIsImgCanVisible: LiveData<Boolean> get() = _saveIsImgCanVisible
    val getIsContentCanVisible: LiveData<Boolean> get() = _saveIsContentCanVisible
    fun f_getContent(step_name:String,step_no:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.get_Content(step_name,step_no)
        }
    }
    fun f_controlvisible(imgVisible:Boolean,contentVisible:Boolean){
        _saveIsImgCanVisible.value = imgVisible
        _saveIsContentCanVisible.value = contentVisible
    }
    override fun onStepContentCallback(str_content: String) {
        _saveStepContentData.value=str_content
    }
}