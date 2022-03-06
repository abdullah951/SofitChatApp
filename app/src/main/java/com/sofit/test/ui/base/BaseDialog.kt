package com.sofit.test.ui.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.sofit.test.R
import com.sofit.test.data.network.NetworkAvailability

abstract class BaseDialog<T : ViewDataBinding, V : BaseViewModel<*>> : DialogFragment() {

    var mActivity: BaseActivity<T, V>? = null
    private var mViewModel: V? = null
    private lateinit var mBinding: T
    private var mRootView: View? = null

    var isConnected = true
    var monitoringConnectivity = false
    lateinit var networkAvailability: NetworkAvailability
    lateinit var ntCallback: ConnectivityManager.NetworkCallback

    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
        }
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(mActivity!!.layoutInflater, getLayoutId(), container, false)
        mRootView = mBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = getViewModel()
        mBinding.setVariable(getBindingVariable(), mViewModel)
        mBinding.executePendingBindings()
    }

    fun getmBinding(): T = mBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // creating the fullscreen dialog
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.window!!.setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog.setCanceledOnTouchOutside(true)

        return dialog
    }



    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String) {
        dismiss()
        getBaseActivity()?.onFragmentDetached(tag)
    }


    fun getBaseActivity(): BaseActivity<T, V>? = mActivity

    fun showLoading() = mActivity?.showLoading()

    fun hideLoading() = mActivity?.hideLoading()

    fun hideKeyboard() = mActivity?.hideKeyboard()

    fun isNetworkConnected(): Boolean = mActivity != null && mActivity!!.isNetworkConnected()

    abstract fun getViewModel(): V

    abstract fun getDataBinding(): T


    fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message, Snackbar.LENGTH_SHORT
        )
        val sbView = snackbar.view
        val textView = sbView
            .findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        snackbar.show()
    }

    fun showMessage(message: String)
    {
        Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
    }


    fun checkConnectivity(myNetwork: NetworkAvailability) {
        val connectivityManager =
            requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo

//        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
//        if (!isConnected) {
        Log.e("internet", " NO NETWORK!")

//            internetDialouge.show(supportFragmentManager, InterNetConnectionDialog.TAG)
//            internetDialouge.isCancelable = false

        // if Network is not connected we will register a network callback to  monitor network
        ntCallback = netWorkCheck(myNetwork)
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(), ntCallback
        )
        monitoringConnectivity = true

//        }
    }

    fun netWorkCheck(nLitener:NetworkAvailability): ConnectivityManager.NetworkCallback {
        val connectivityCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
//                    isConnected = true
                    nLitener.onNetworkAvailable()
                    Log.e("Internet", "INTERNET CONNECTED")
                }

                override fun onLost(network: Network) {
//                    isConnected = false
                    nLitener.onNetworkLost()
                    Log.e("Internet", "INTERNET LOST")
                }

            }
        return connectivityCallback
    }



    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
}