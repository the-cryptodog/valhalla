package com.app.valhalla.data.model

sealed class MainObject {
    object OBJ_TABLE : MainObject()
    object OBJ_TABLE_SMALL : MainObject()
    object OBJ_INCENSE_BURNER : MainObject()
    object OBJ_VASE : MainObject()
    object OBJ_JOSS : MainObject()
    object OBJ_JOSS_BACKGROUND : MainObject()
    object OBJ_CANDLE : MainObject()
    object BACK : MainObject()
    object DRAWING : MainObject()
    object LOT : MainObject()
    object TABLE_MAP : MainObject()

    sealed class Panel : MainObject() {

        var keyName: String = ""
        var imgUrl: String = ""
        var isClicked: Boolean = false
        var isPressed: Boolean = false

        fun imgUrl(): String {
            return imgUrl
        }

        object BOTTOM_LEFT : Panel()
        object BOTTOM_CENTER : Panel()
        object BOTTOM_RIGHT : Panel()
    }
}
