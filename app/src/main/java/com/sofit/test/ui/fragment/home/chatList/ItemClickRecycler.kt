package com.sofit.test.ui.fragment.home.chatList

import com.sofit.test.data.model.User

interface ItemClickRecycler {
    fun addFriendToList(user: User)
}