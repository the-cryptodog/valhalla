package com.app.valhalla.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RemoteByeResponse(

    @SerializedName("result")
    var result: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("AppStatus")
    val appStatus: AppStatus,
    @SerializedName("property_contents")
    val property_contents: PropertyContents? = null,
    @SerializedName("data")
    val data: List<GameObjects>,
    @SerializedName("morality")
    val morality: List<MoralityItem>

)

@Parcelize
data class MoralityItem(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("things_id")
    val thingsId: String,
    @SerializedName("dead_datetime")
    val deadDatetime: String
) : Parcelable



