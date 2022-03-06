package com.sofit.test.di.module.activity

import androidx.lifecycle.ViewModelProvider
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.activity.splash.SplashViewModel
import com.sofit.test.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by JAI on 18,November,2019
 * JAI KHAMBHAYTA
 */
@Module
@InstallIn(ApplicationComponent::class)
class SplashActivityModule {

    @Provides
    internal fun provideSplasViewModel(dataManager: DataManager): SplashViewModel {
        return SplashViewModel(dataManager)
    }

    @Provides
    internal fun mainActivityModelProvider(splashViewModel: SplashViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(splashViewModel)
    }


}