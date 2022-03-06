package com.sofit.test.ui.fragment.profile

import com.sofit.test.ui.base.BaseView

interface ProfileNavigator : BaseView {
    fun updateUser(name: String)
}