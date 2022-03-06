package com.sofit.test.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sofit.test.data.model.*
import com.sofit.test.data.source.dao.AllUserDao
import com.sofit.test.data.source.dao.ChatDao
import javax.inject.Singleton

/**
 * Created by JAI on 12,November,2019
 * JAI KHAMBHAYTA
 */
@Singleton
@Database(entities = [User::class, Chat::class], version = 5, exportSchema = false)
@TypeConverters(PositionConverters::class)
abstract class LocalDb : RoomDatabase() {
    abstract fun allUserDao(): AllUserDao
    abstract fun chatDao(): ChatDao
}
