package com.sofit.test.ui.activity.signup

import com.sofit.test.ui.base.BaseView

interface SignupNavigator : BaseView {

    fun navigateToLogin()
//    fun createNewUser(email: String, password: String)
    fun navigateToSignUp2(email: String, password: String)

}