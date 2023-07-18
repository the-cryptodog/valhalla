package com.app.valhalla.util

import java.util.*

object GlobalUID {
    private var saveUID =""
    fun updateSavedUID(newUID :String){
        saveUID = newUID
    }

    fun getSavedUID() = saveUID
    }

