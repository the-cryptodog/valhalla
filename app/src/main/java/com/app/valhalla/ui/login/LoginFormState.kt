package com.app.valhalla.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val emailError: Int? = null,
    val nicknameError: Int? = null,
    val pwdError: Int? = null,
    val isDataValid: Boolean = false,
    val isShow: Boolean = false
)