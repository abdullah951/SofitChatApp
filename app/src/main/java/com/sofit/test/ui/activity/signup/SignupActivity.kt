package com.sofit.test.ui.activity.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivitySignupBinding
import com.sofit.test.databinding.ActivitySplahBinding
import com.sofit.test.ui.activity.login.LoginActivity
import com.sofit.test.ui.activity.signu2.Signup2Activity
import com.sofit.test.ui.base.BaseActivity
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>(),SignupNavigator {

    @Inject
    lateinit var mSignupViewModel: SignupViewModel

    @Inject
    lateinit var mAppPreferences: AppPreferences

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_signup

    override fun getViewModel(): SignupViewModel = mSignupViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        mSignupViewModel.setNavigator(this)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun navigateToLogin() {
        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
/*
    override fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    mAppPreferences.setString(AppConstant.PREF_NAME, email)
                    navigateToSignUp2(email, password)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    */


    override fun navigateToSignUp2(email: String, password: String) {
        val intent = Intent(this@SignupActivity,Signup2Activity::class.java)
        intent.putExtra("email",email)
        intent.putExtra("password", password)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }
    companion object {
        const val TAG = "SignupActivity"
    }
}