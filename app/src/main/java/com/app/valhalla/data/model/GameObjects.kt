package com.app.valhalla.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameObjects(
    @SerializedName("category_id")
    var category_id: String = "",
    @SerializedName("things_data")
    var things_data: List<GameObject>
    ): Parcelable




@Parcelize
data class GameObject(
    @SerializedName("things_id")
    var id: String = "",
    @SerializedName("things_name")
    var name: String = "",
    @SerializedName("img_url")
    var img_url: String,
    @SerializedName("timer")
    var timer: Int = 0,
    var isSelected: Boolean = false,
    @SerializedName("is_default")
    var is_default: Boolean,
    @SerializedName("dollars")
    var dollars: Int,
    @SerializedName("use_status")
    var use_status: Int
) : Parcelable {
    fun imgUrl(): String {
        return img_url
    }
}

@Parcelize
data class BaseResult(

    @SerializedName("result")
    var result: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("AppStatus")
    val appStatus: AppStatus,
    @SerializedName("property_contents")
    val property_contents: PropertyContents? = null,
    @SerializedName("data")
    val data: List<GameObjects>
) : Parcelable

@Parcelize
data class AppStatus(
    @SerializedName("AppVersion")
    val appVersion: String,
    @SerializedName("isMustUpdate")
    val isMustUpdate: Int,
    @SerializedName("MarketUrl")
    val marketUrl: String,
    @SerializedName("isfix")
    val isFixing: Int
) : Parcelable

@Parcelize
data class PropertyContents(

    val id: Int,

    @SerializedName("ApiResult")
    val ApiList: String,

    @SerializedName("is_hassameproperty")
    val isHasSameProperty: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("hint")
    val hint: String,

    @SerializedName("scores")
    val scores: Int

) : Parcelable

