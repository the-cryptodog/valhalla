package com.app.valhalla.data.model
import android.os.Parcelable
import com.app.valhalla.util.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
data class StepObject (
    var name: String = "",
    var img_url: String,
    var step_count: Int,
    var step_source: String
) : Parcelable{
    fun imgUrl(): String {
        return img_url
    }
    fun StepResource(): String{
        return step_source
    }
}

@Parcelize
data class StepBaseResult (
    var result: String = "",
    var message: String = "",
    var data : StepObject
) : Parcelable