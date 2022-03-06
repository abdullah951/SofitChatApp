package com.sofit.test.ui.base

import OnOneOffClickListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.sofit.test.data.model.User
import com.sofit.test.ui.activity.chatActivity.ChatActivity
import com.sofit.test.ui.fragment.home.HomeViewModel
import com.sofit.test.ui.fragment.profile.ProfileViewModel

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(),BaseView {

    var mActivity: BaseActivity<T, V>? = null
    lateinit var mRootView: View
    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V
    abstract fun getBindingVariable(): Int
    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): V
    abstract fun getLifeCycleOwner(): LifecycleOwner
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context
            mActivity = activity as BaseActivity<T, V>?
            mActivity?.onFragmentAttached()
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(getBindingVariable(), getViewModel())
        mViewDataBinding.lifecycleOwner = getLifeCycleOwner()
        mViewDataBinding.executePendingBindings()


        // setup title
        when (getViewModel()) {
            is HomeViewModel -> {
//                (activity as MainActivity).setTitle(getString(R.string.title_home))
            }

            is ProfileViewModel -> {
//                (activity as MainActivity).setTitle(getString(R.string.title_profile))
            }

            else -> {
//                (activity as MainActivity).setTitle(getString(R.string.app_name))
            }
        }

    }

    fun setTitle(tile: String){
//        (activity as MainActivity).setTitle(tile,true)
    }

    fun setEmptyView(isEmpaty: Boolean = false) {
//        (activity as MainActivity).setEmptyView(isEmpaty)
    }

    private fun performDependencyInjection() {
//        AndroidSupportInjection.inject(this)
    }

    fun getBaseActivity(): BaseActivity<T, V>? = mActivity

    fun getViewDataBinding(): T = mViewDataBinding

    fun showLoading() = mActivity?.showLoading()

    fun hideLoading() = mActivity?.hideLoading()

    fun hideKeyboard() = mActivity?.hideKeyboard()

    override fun isNetworkConnected(): Boolean = mActivity != null && mActivity!!.isNetworkConnected()

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    override fun showMessage(message: String) {
        mActivity?.showMessage(message)
    }
    override fun showMessage(resId: Int) {
        Toast.makeText(requireActivity(), getString(resId), Toast.LENGTH_SHORT).show()
    }
    fun onError(message: String?) {
        mActivity?.onError(message)
    }

    override fun showSnackBar(view: View, message: String) {
        var view = view
        val snackbar = Snackbar.make(view, message!!, Snackbar.LENGTH_LONG)
        view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        params.setMargins(0, 0, 0, 150)
        snackbar.setActionTextColor(Color.RED)
        snackbar.show()
    }
    override fun showSnackBar(message: String) {
//        val snackbar = Snackbar.make(
//            requireActivity().findViewById(android.R.id.content),
//            message, Snackbar.LENGTH_SHORT
//        )
//        val sbView = snackbar.view
//        val textView = sbView
//            .findViewById<View>(R.id.snackbar_text) as TextView
//        textView.setTextColor(ContextCompat.getColor(mActivity, R.color.white))
//        snackbar.show()
    }

    override fun showNetworkMessage() {
//        Toast.makeText(this, getString(R.string.txt_NoInternet), Toast.LENGTH_SHORT).show()
    }
    /**
     * prevent double click on view
     **/
    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        try {
            val safeClickListener = OnOneOffClickListener {
                onSafeClick(it)
            }
            setOnClickListener(safeClickListener)
        } catch (E: Exception) {
        }
    }

    fun moveToChatActivity(user: User) {
        requireActivity().startActivity(Intent(context, ChatActivity::class.java)
            .putExtra("friendName", user.userName)
            .putExtra("friendId", user.userId)
            .putExtra("profilePic", user.profileImage))
    }

}
