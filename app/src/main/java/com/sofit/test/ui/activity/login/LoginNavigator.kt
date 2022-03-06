package com.sofit.test.ui.activity.login

import com.sofit.test.ui.base.BaseView

interface LoginNavigator : BaseView {

    fun navigateToSignUp()
    fun signInEmail(email: String, password: String)

}