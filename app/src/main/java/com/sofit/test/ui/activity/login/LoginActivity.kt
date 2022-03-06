package com.sofit.test.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivityLoginBinding
import com.sofit.test.databinding.ActivitySplahBinding
import com.sofit.test.ui.activity.main.MainActivity
import com.sofit.test.ui.activity.signu2.Signup2Activity
import com.sofit.test.ui.activity.signup.SignupActivity
import com.sofit.test.ui.base.BaseActivity
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(),LoginNavigator {

    @Inject
    lateinit var mLoginViewModel: LoginViewModel

    @Inject
    lateinit var mAppPreferences: AppPreferences

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModel(): LoginViewModel = mLoginViewModel

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        mLoginViewModel.setNavigator(this)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun navigateToSignUp() {
        startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish()
    }

    override fun signInEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    mAppPreferences.setString(AppConstant.PREF_NAME, email)
                    navigateToDashboard()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }

}