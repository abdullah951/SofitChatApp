package com.sofit.test.data.source.preference

import android.content.Context
import android.content.SharedPreferences
import com.sofit.test.utils.AppConstant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by JAI on 14,November,2019
 * JAI KHAMBHAYTA
 */
@Singleton
class AppPreferences @Inject constructor(mContext: Context){


    private var mPrefs: SharedPreferences = mContext.getSharedPreferences(AppConstant.PREF_NAME, Context.MODE_PRIVATE)


    fun getString(key: String): String {
        return mPrefs.getString(key, null).toString()
    }


    fun setString(key: String, value: String) {
        mPrefs.edit().putString(key, value).apply()
    }

    fun clearSharedPreference() {
        mPrefs.edit().clear()
        mPrefs.edit().apply()
    }

}