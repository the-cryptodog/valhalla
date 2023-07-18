package com.app.valhalla.data.model
import android.os.Parcelable
import com.app.valhalla.util.Constant
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class StepBaseResult (
    @SerializedName("result")
    var result: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("AppStatus")
    val appStatus: AppStatus,
    @SerializedName("property_contents")
    val property_contents: StepPropertyContents,
    @SerializedName("data")
    val data: List<GameObjects>? = null
) : Parcelable

@Parcelize
data class StepPropertyContents(

    @SerializedName("god_name")
    val GodName: String,

    @SerializedName("img_url")
    val ImgUrl: String,

    @SerializedName("step_count")
    val StepCount: Int,

    @SerializedName("step_source")
    val StepSource: String

) : Parcelable
