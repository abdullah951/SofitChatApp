package com.sofit.test.ui.fragment.home.chatList

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.*
import com.sofit.test.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatListViewModel @Inject constructor(val dataManager: DataManager) : BaseViewModel<ChatListNavigator>(dataManager) {

    var nameText: ObservableField<String> = ObservableField<String>()

    suspend fun fetchAllUserRemote(): Pair<Int, String> {

        return withContext(Dispatchers.IO) {
            var msg: Pair<Int, String>
            if (dataManager.allUserDao.getSize() == 0) {
                val data = getNavigator()?.alllist()
            }
            msg = Pair(0, "")
            msg
        }
    }

    suspend fun fetchDataFromDatabase(): LiveData<List<User>> {
        return withContext(Dispatchers.IO) {
            dataManager.allUserDao.getAllData()
        }
    }

    suspend fun insertAllUserDatabase(data: ArrayList<User>) {
        return withContext(Dispatchers.IO) {
            dataManager.insertAllUserData(data)
        }
    }

    fun onSignUp() {

    }

}