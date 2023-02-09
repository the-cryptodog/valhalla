package com.app.valhalla.data

import com.app.valhalla.data.model.BaseResult

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val baseResult: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$baseResult]"
            is Error -> "Error[exception=$exception]"
        }
    }
}