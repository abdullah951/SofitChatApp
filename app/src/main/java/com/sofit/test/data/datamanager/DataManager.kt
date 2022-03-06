package com.sofit.test.data.datamanager

import com.sofit.test.data.model.Chat
import com.sofit.test.data.model.User
import com.sofit.test.data.source.NetworkCall
import com.sofit.test.data.source.dao.AllUserDao
import com.sofit.test.data.source.dao.ChatDao
import com.sofit.test.ui.base.BaseRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by JAI on 11,November,2019
 * JAI KHAMBHAYTA
 */

@Singleton
class DataManager @Inject constructor(
    val allUserDao: AllUserDao,
    val chatDao: ChatDao,
    val networkCall: NetworkCall
) : BaseRepository() {

    /**
     * API call
     */

    suspend fun fetchCountryData(): Any {
        val data =
            safeApiCall({ networkCall.getAllData() }, "No response")
        return data!!
    }


    /**
     * DATABASE Manage
     */

    fun insertAllUserData(data: ArrayList<User>): Any {
        val result = allUserDao.insertAll(data)
        return result
    }

    fun insertChatData(data: ArrayList<Chat>): Any {
        val result = chatDao.insertAll(data)
        return result
    }



}