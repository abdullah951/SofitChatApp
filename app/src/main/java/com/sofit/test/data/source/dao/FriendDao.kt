package com.sofit.test.data.source.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofit.test.data.model.User

interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<User>)

    @Query("SELECT * FROM alluser WHERE userId = :id")
    fun fetchAllData(id: Int): LiveData<List<User>>

    @Query("SELECT * FROM alluser")
    fun getAllData(): LiveData<List<User>>


    @Query("SELECT count(*) FROM alluser")
    fun getSize():Int

}