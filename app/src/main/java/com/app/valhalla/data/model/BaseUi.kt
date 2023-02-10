package com.app.valhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BaseUi(
    val keyName: String,
    var imgUrl: String ="",
    var isClicked: Boolean = false,
    var isPressed: Boolean =false
) : Parcelable{
    fun imgUrl(): String {
        return imgUrl
    }
}

