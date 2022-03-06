package com.sofit.test.ui.activity.chatActivity

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.Chat
import com.sofit.test.data.model.User
import com.sofit.test.ui.base.BaseViewModel
import com.sofit.test.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatViewModel @Inject constructor(val dataManger: DataManager) : BaseViewModel<ChatNavigator>(dataManger) {

    var emailText: ObservableField<String> = ObservableField<String>()
    var passwordText: ObservableField<String> = ObservableField<String>()
    var confirmPasswordText: ObservableField<String> = ObservableField<String>()


    fun onLoginUp() {
    }

    fun onSignUp() {
    }

    suspend fun fetchDataFromDatabase(): LiveData<List<Chat>> {
        return withContext(Dispatchers.IO) {
            dataManger.chatDao.getAllData()
        }
    }


    suspend fun insertChatDatabase(data: ArrayList<Chat>) {
        return withContext(Dispatchers.IO) {
            dataManger.insertChatData(data)
        }
    }

}