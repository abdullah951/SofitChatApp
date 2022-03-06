package com.sofit.test.ui.fragment.home.chatList.adapter

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.sofit.test.data.model.User

/**
 * Created by JAI on 15,November,2019
 * JAI KHAMBHAYTA
 */
class ChatListItemViewModel(var user: User, var mListener: MovieItemViewModelListener) {

    var imageUrl = MutableLiveData<String>()
    var nameText: ObservableField<String> = ObservableField<String>()
    var countryText: ObservableField<String> = ObservableField<String>()

    init {
        imageUrl.value = user.profileImage
        nameText.set(user.userName)
        countryText.set(user.country)
    }

    fun onItemClick() {
        Log.d("TAG", "onItemClick: ")
        mListener.onItemClick(user)
    }


    interface MovieItemViewModelListener {
        fun onItemClick(movie: User)
    }
}