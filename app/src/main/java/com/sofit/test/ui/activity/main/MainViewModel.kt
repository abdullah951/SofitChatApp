package com.sofit.test.ui.activity.main

import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(val dataManager: DataManager) : BaseViewModel<MainNavigator>(dataManager)
