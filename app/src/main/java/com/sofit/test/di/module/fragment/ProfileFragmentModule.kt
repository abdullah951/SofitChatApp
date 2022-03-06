package com.sofit.test.di.module.fragment

import androidx.lifecycle.ViewModelProvider
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.fragment.profile.ProfileViewModel
import com.sofit.test.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by JAI on 15,November,2019
 * JAI KHAMBHAYTA
 */

@Module
@InstallIn(ApplicationComponent::class)
class ProfileFragmentModule {
    @Provides
    internal fun provideProfileViewModel(dataManager: DataManager): ProfileViewModel {
        return ProfileViewModel(dataManager)
    }


    @Provides
    fun provideViewModelFactory(profileViewModel: ProfileViewModel): ViewModelProvider.Factory =
        ViewModelProviderFactory(profileViewModel)

}