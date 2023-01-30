package com.app.valhalla.ui.drawlots

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawLotsViewModel: ViewModel() {
    private val _saveStepVisible = MutableLiveData<Boolean>()
    val getStepData: LiveData<Boolean> get() = _saveStepVisible


    fun StepVisible(){
        _saveStepVisible.value = true
        viewModelScope.launch(Dispatchers.IO){
            delay(3*1000)
            withContext(Dispatchers.Main){
                _saveStepVisible.value = false
            }
        }
    }
}