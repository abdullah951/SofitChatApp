package com.sofit.test.di.module

import android.content.Context
import androidx.room.Room
import com.sofit.test.data.source.LocalDb
import com.sofit.test.data.source.dao.AllUserDao
import com.sofit.test.data.source.dao.ChatDao
import com.sofit.test.utils.AppConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by JAI on 12,November,2019
 * JAI KHAMBHAYTA
 */
@Module
@InstallIn(ApplicationComponent::class)
class DbModule {
    //
    @Provides
    @Singleton
    internal fun provideDb(context: Context): LocalDb {
        return Room.databaseBuilder(context, LocalDb::class.java, AppConstant.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideAllUserDao(context: Context): AllUserDao {
        return provideDb(context).allUserDao()
    }

    @Provides
    @Singleton
    internal fun provideChatDao(context: Context): ChatDao {
        return provideDb(context).chatDao()
    }

}