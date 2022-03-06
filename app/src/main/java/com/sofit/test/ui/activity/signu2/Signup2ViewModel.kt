package com.sofit.test.ui.activity.signup

import androidx.databinding.ObservableField
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.ResponseCountryX
import com.sofit.test.data.model.ResponsePlayers
import com.sofit.test.data.network.NetworkResult
import com.sofit.test.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class Signup2ViewModel @Inject constructor(val dataManger: DataManager) : BaseViewModel<Signup2Navigator>(dataManger) {

    var nameText: ObservableField<String> = ObservableField<String>()

    suspend fun fetchDataFromDatabase(): ArrayList<String> {
        return withContext(Dispatchers.IO) {
            var array: ArrayList<String> =  ArrayList()
            val data = dataManger.fetchCountryData()
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
        getNavigator()?.createUser(nameText.get().toString())
    }

}