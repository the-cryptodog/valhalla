package com.app.valhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StepContentObject (
    var content: String
)

data class StepContentBaseResult (
    var result: String = "",
    var message: String = ""
)