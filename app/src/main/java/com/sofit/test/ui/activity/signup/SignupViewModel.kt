package com.sofit.test.ui.activity.signup

import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sofit.test.data.datamanager.DataManager
import com.sofit.test.ui.base.BaseViewModel
import com.sofit.test.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupViewModel @Inject constructor(val dataManger: DataManager) : BaseViewModel<SignupNavigator>(dataManger) {

    var emailText: ObservableField<String> = ObservableField<String>()
    var passwordText: ObservableField<String> = ObservableField<String>()
    var confirmPasswordText: ObservableField<String> = ObservableField<String>()


    fun onLoginUp() {
        getNavigator()?.navigateToLogin()
    }

    fun onSignUp() {
        Log.d("TAG", "onSignUp: ")
        if(validate()) {
            emailText.get()?.let { passwordText.get()
                ?.let { it1 -> getNavigator()?.navigateToSignUp2(it, it1) } }
        }
    }

    private fun validate(): Boolean {
        var msg: String? = null
        Log.d("TAG", "validate: "+emailText.get()+" "+passwordText.get())
        when {
            emailText.get()?.isEmpty() == true -> {
                msg = "Please Enter Email Address"
            }
            passwordText.get()?.isEmpty() == true -> {
                msg = "Please Enter Password"
            }
            emailText.get()?.let { AppUtils.isValidEmail(it) } == false -> {
                msg = "Please enter correct Email form"
            }
            passwordText.get()?.equals(confirmPasswordText.get()) == false -> {
                msg = "Please Enter correct Password"
            }
        }
        if (msg != null) {
            Log.d("TAG", "validate: $msg")
            getNavigator()?.showMessage(msg)
            return false
        }
        Log.d("TAG", "validate: ")
        return true
    }

}