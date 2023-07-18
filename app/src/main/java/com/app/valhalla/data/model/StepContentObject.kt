package com.app.valhalla.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StepContentObject (
    var step_content: String
)

data class StepContentBaseResult (
    var result: String = "",
    var message: String = "",
    var property_contents: StepContentObject
)