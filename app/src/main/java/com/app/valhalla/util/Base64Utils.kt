package com.app.valhalla.util

import android.util.Base64

object Base64Utils {
    fun base64decodebitmap(input_base64:String):ByteArray{
        var imageByteArray = Base64.decode(input_base64,Base64.DEFAULT)
        return imageByteArray
    }
}