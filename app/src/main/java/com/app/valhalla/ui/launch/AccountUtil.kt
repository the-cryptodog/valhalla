package com.app.valhalla.ui.launch

object AccountUtil {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$")
        return email.matches(emailRegex)
    }

    // 密碼至少包含六個英文字母或數字
    fun isPasswordValid(password: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]{5,}$")
        return password.matches(regex) && password.length >= 5
    }

    // 暱稱至少包含一個中文字且長度在1到6個字之間
    fun isNickNameValid(nickName: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]{1,16}$")
        return nickName.matches(regex)
    }
}