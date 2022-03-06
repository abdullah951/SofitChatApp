package com.sofit.test.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(obj: Any) : ViewModel() {

    private var mNavigator: WeakReference<N>? = null
    var loader: ObservableBoolean = ObservableBoolean(false)
    //    val error: MutableLiveData<ErrorData?> = MutableLiveData()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun displayLoader(isLoading: Boolean) {
        loader.set(isLoading)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }

    fun getNavigator(): N? {
        return mNavigator?.get()
    }

}