package com.app.valhalla.data.model
import android.os.Parcelable
import com.app.valhalla.util.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
data class StepObject (
    var name: String = "",
    var img_url: String,
    var step_count: Int
) : Parcelable{
    fun imgUrl(): String {
        return Constant.BASE_URL+img_url
    }
}

@Parcelize
data class StepBaseResult (
    var result: String = "",
    var message: String = "",
    val data : StepObject
) : Parcelable