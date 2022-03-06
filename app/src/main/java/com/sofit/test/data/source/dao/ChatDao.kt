package com.sofit.test.data.source.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofit.test.data.model.Chat

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Chat>)

    @Query("SELECT * FROM chat WHERE receiverId = :id")
    fun fetchAllData(id: Int): LiveData<List<Chat>>

    @Query("SELECT * FROM chat")
    fun getAllData(): LiveData<List<Chat>>


    @Query("SELECT count(*) FROM chat")
    fun getSize():Int

}