package com.app.valhalla.data.model

import android.os.Parcelable
import com.app.valhalla.util.Constant
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameObject(
    @SerializedName("things_id")
    var id: String = "",
    @SerializedName("things_name")
    var name: String = "",
    @SerializedName("category_id")
    var type: String = "",
    @SerializedName("img_url")
    var img_url: String,
    @SerializedName("timer")
    var timer: Int = 0,
    var isSelected: Boolean = false,
    @SerializedName("is_default")
    var is_default: Boolean
//    var description: String = "",
//    var image: String = "",
//    var price: Double = 0.0,
//    var rating: Double = 0.0,
//    var type: String = "",
//    var category: String = "",
//    var tags: List<String> = emptyList(),
//    var status: String = "",
//    var createdAt: String = "",
//    var updatedAt: String = "",
//    var deletedAt: String = "",
//    var favorite: Boolean = false,
//    var favoriteCount: Int = 0,
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
    val data: List<GameObject>
) : Parcelable

@Parcelize
data class AppStatus(
    @SerializedName("AppVersion")
    val appVersion: String,
    @SerializedName("isMustUpdate")
    val isMustUpdate: Int,
    @SerializedName("MarketUrl")
    val marketUrl: String
) : Parcelable

@Parcelize
data class PropertyContents(
    val id:Int
) : Parcelable