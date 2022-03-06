package com.sofit.test.di.module.activity

import androidx.lifecycle.ViewModelProvider
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.activity.login.LoginViewModel
import com.sofit.test.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class LoginActivityModule {

    @Provides
    internal fun provideLoginViewModel(dataManager: DataManager): LoginViewModel {
        return LoginViewModel(dataManager)
    }

    @Provides
    internal fun mainActivityModelProvider(loginViewModel: LoginViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(loginViewModel)
    }


}
