package com.sofit.test.ui.activity.splash

import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.base.BaseViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(dataManger: DataManager) : BaseViewModel<SplashNavigator>(dataManger)