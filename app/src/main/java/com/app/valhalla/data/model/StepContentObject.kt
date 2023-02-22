package com.app.valhalla.data.model


data class StepContentObject (
    var content: String = ""
)

data class StepContentBaseResult (
    var result: String = "",
    var message: String = "",
    val data : StepContentObject
)