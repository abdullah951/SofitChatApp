package com.sofit.test.di.module

import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.source.NetworkCall
import com.sofit.test.data.source.dao.AllUserDao
import com.sofit.test.data.source.dao.ChatDao
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
class RepoModule {
    @Provides
    @Singleton
    internal fun provideMovieRepository(
        allUserDao: AllUserDao,
        chatDao: ChatDao,
        neworkCall: NetworkCall
    ): DataManager {
        return DataManager(allUserDao,chatDao, neworkCall)
    }


}