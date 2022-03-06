package com.sofit.test.ui.fragment.profile

import androidx.databinding.ObservableField
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.ResponseCountryX
import com.sofit.test.data.network.NetworkResult
import com.sofit.test.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val dataManager: DataManager) : BaseViewModel<ProfileNavigator>(dataManager) {

    var nameText: ObservableField<String> = ObservableField<String>()

    suspend fun fetchDataFromDatabase(): ArrayList<String> {
        return withContext(Dispatchers.IO) {
            var array: ArrayList<String> =  ArrayList()
            val data = dataManager.fetchCountryData()
            when ((data as NetworkResult<Any>)) {
                is NetworkResult.Success<Any> -> {

                    ((data as NetworkResult.Success<*>).data as ResponseCountryX).forEach {
                        array.add(it.name)
                    }
                }
                is NetworkResult.Error -> {
//
                }
            }
            array
        }
    }

    fun onSignUp() {
        getNavigator()?.updateUser(nameText.get().toString())
    }

}