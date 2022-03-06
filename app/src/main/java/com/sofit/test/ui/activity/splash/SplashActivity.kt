package com.sofit.test.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivitySplahBinding
import com.sofit.test.ui.activity.login.LoginActivity
import com.sofit.test.ui.activity.main.MainActivity
import com.sofit.test.ui.base.BaseActivity
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplahBinding, SplashViewModel>(),SplashNavigator {

    @Inject
    lateinit var mSplashViewModel: SplashViewModel

    @Inject
    lateinit var mAppPreferences: AppPreferences
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_splah

    override fun getViewModel(): SplashViewModel = mSplashViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        mSplashViewModel.setNavigator(this)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        Handler().postDelayed({
            if(currentUser == null){
                Log.d(TAG,"Not Logged in, Move to Login page")
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish()
            } else {
                Log.d(TAG,"Logged in move to dashboard")
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish()
            }
        }, 2000)


    }

    companion object {
        const val TAG = "SplashActivity"
    }
}