package com.app.valhalla.data

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    sealed class LoadingViewState {
        data class ShowLoadingView(
            val message: String? = null
        ) : LoadingViewState()

        object HideLoadingView : LoadingViewState()

        object MainActivityImageLoadingDone : LoadingViewState()
    }

    public val loadingViewStatePublisher = MutableLiveData<LoadingViewState>()

}