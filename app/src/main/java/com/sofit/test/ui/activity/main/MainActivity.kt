package com.sofit.test.ui.activity.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.sofit.test.BR
import com.sofit.test.ui.base.BaseActivity

import com.sofit.test.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.amitshekhar.DebugDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivityMainBinding
import com.sofit.test.ui.activity.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),MainNavigator{

    @Inject
    lateinit var mMainViewModel: MainViewModel
    @Inject
    lateinit var sharedPreferences: AppPreferences
    private lateinit var mActivityMainBinding: ActivityMainBinding
    private lateinit var mContext: Context
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun getViewModel(): MainViewModel = mMainViewModel

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mActivityMainBinding = getViewDataBinding()
        mMainViewModel.setNavigator(this)
        DebugDB.getAddressLog()
        setupFragment()
    }

    private fun setupFragment() {
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        val signoutMenuItem = mActivityMainBinding.navView.menu.findItem(R.id.navigation_logout)
        signoutMenuItem.setOnMenuItemClickListener {
            Firebase.auth.signOut()
            sharedPreferences.clearSharedPreference()
            navigateToLogin()
            true
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish()
    }


    // setup appbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    fun thi()
    {

    }





}
