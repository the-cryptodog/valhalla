package com.app.valhalla.data

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    sealed class LoadingViewState {
        data class ShowLoadingView(
            @StringRes val stringResId: Int? = null,
            val message: String? = null
        ) : LoadingViewState()

        object HideLoadingView : LoadingViewState()
        object LoadingDone : LoadingViewState()
    }

    public val loadingViewStatePublisher = MutableLiveData<LoadingViewState>()

}