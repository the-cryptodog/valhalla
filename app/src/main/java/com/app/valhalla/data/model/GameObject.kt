package com.app.valhalla.data.model
import android.os.Parcelable
import com.app.valhalla.util.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameObject (

    var id: String = "",
    var name: String = "",
    var type: String = "",
    var img_url: String,
    var timer: Int = 0,
    var isSelected: Boolean = false,
    var is_default: Boolean = false
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
) : Parcelable{
    fun imgUrl(): String {
        return img_url
    }
}

@Parcelize
data class BaseResult (

    var result: String = "",
    var message: String = "",
    val data : List<GameObject>
) : Parcelable