package com.sofit.test.ui.fragment.home.friendList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sofit.test.data.model.User
import com.sofit.test.databinding.ItemChatListBinding
import com.sofit.test.databinding.ItemFriendListBinding
import com.sofit.test.ui.base.BaseViewHolder
import com.sofit.test.ui.fragment.home.friendList.ItemClickChat

/**
 * Created by JAI on 15,November,2019
 * JAI KHAMBHAYTA
 */
class FriendListAdapter(var itemClickChat: ItemClickChat, var teamList: List<User>) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val mMovieViewBinding = ItemFriendListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TeamViewHolder(mMovieViewBinding)

    }

    override fun getItemCount(): Int = teamList.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class TeamViewHolder(val mMovieViewBinding: ItemFriendListBinding?) :
        BaseViewHolder(mMovieViewBinding!!.root), FriendListItemViewModel.MovieItemViewModelListener {
        override fun onItemClick(user: User) {
            itemClickChat.openChat(user)
        }

        private lateinit var teamItemViewModel: FriendListItemViewModel
        override fun onBind(position: Int) {
            val movie = teamList[position]
            teamItemViewModel = FriendListItemViewModel(movie, this)
            mMovieViewBinding!!.viewModel = teamItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mMovieViewBinding!!.executePendingBindings()
        }
    }
}


