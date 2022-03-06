package com.sofit.test.ui.fragment.home.chatList

import com.sofit.test.data.model.User
import com.sofit.test.ui.base.BaseView

interface ChatListNavigator : BaseView {
    fun alllist(): ArrayList<User>
}