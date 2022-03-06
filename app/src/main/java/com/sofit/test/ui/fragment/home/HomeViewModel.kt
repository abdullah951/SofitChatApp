package com.sofit.test.ui.fragment.home

import androidx.lifecycle.LiveData
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.data.model.ResponseTeam
import com.sofit.test.data.model.Team
import com.sofit.test.data.network.NetworkResult
import com.sofit.test.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class HomeViewModel @Inject constructor(val dataManager: DataManager) : BaseViewModel<HomeNavigator>(dataManager) {

}
