package com.sofit.test.di.module.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.Team
import com.sofit.test.ui.fragment.home.HomeFragment
import com.sofit.test.ui.fragment.home.HomeViewModel
import com.sofit.test.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by JAI on 13,November,2019
 * JAI KHAMBHAYTA
 */
@Module
@InstallIn(ApplicationComponent::class)
class HomeFragmentModule {

    @Provides
    internal fun provideHomeViewModel(dataManager: DataManager): HomeViewModel {
        return HomeViewModel(dataManager)
    }



    @Provides
    fun provideViewModelFactory(homeViewModel: HomeViewModel): ViewModelProvider.Factory =
        ViewModelProviderFactory(homeViewModel)


}