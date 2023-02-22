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
    val getStepContentData: LiveData<String> get() = _saveStepContentData
    fun f_getContent(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.get_Content()
        }
    }

    override fun onStepContentCallback(str_content: String) {
        _saveStepContentData.value=str_content
    }

}