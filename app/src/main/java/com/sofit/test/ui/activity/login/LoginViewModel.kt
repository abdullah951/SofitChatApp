package com.sofit.test.ui.activity.login

import androidx.databinding.ObservableField
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.base.BaseViewModel
import com.sofit.test.utils.AppUtils
import javax.inject.Inject

class LoginViewModel @Inject constructor(dataManger: DataManager) : BaseViewModel<LoginNavigator>(dataManger){

    var emailText: ObservableField<String> = ObservableField<String>()
    var passwordText: ObservableField<String> = ObservableField<String>()

    fun onSignUp() {
        getNavigator()?.navigateToSignUp()
    }

    fun onLogin() {
        if(validate()) {
            emailText.get()?.let { passwordText.get()
                ?.let { it1 -> getNavigator()?.signInEmail(it, it1) } }
        }
    }

    private fun validate(): Boolean {
        var msg: String? = null
        when {
            emailText.get()?.isEmpty() == true -> {
                msg = "Please Enter Email Address"
            }
            passwordText.get()?.isEmpty() == true -> {
                msg = "Please Enter Password"
            }
            emailText.get()?.let { AppUtils.isValidEmail(it) } == false -> {
                msg = "Please enter correct Email format"
            }
        }
        if (msg != null) {
            getNavigator()?.showMessage(msg)
            return false
        }
        return true
    }

}