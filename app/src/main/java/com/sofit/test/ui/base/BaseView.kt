package com.sofit.test.ui.base

import android.view.View

interface BaseView {

        fun showMessage(message: String)
        fun showMessage(resId: Int)
//        fun showMessage(e: CustomException)
        fun showSnackBar(message: String)
        fun showSnackBar(view: View, message: String)
        fun isNetworkConnected(): Boolean
        fun showNetworkMessage()

}