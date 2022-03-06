package com.sofit.test.ui.fragment.home.chatFragment.adapter

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.sofit.test.data.model.Chat
import com.sofit.test.data.model.User

/**
 * Created by JAI on 15,November,2019
 * JAI KHAMBHAYTA
 */
class ChatItemViewModel(var chat: Chat, var mListener: MovieItemViewModelListener) {

    var imageUrl = MutableLiveData<String>()
    var chatText: ObservableField<String> = ObservableField<String>()

    init {
        imageUrl.value = chat.receiverId
        chatText.set(chat.message)
    }

    fun onItemClick() {
        Log.d("TAG", "onItemClick: ")
        mListener.onItemClick(chat)
    }


    interface MovieItemViewModelListener {
        fun onItemClick(movie: Chat)
    }
}