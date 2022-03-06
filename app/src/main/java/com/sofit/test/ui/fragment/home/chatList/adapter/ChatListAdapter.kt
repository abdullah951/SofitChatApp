package com.sofit.test.ui.fragment.home.chatList.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sofit.test.data.model.User
import com.sofit.test.databinding.ItemChatListBinding
import com.sofit.test.ui.base.BaseViewHolder
import com.sofit.test.ui.fragment.home.chatList.ItemClickRecycler

/**
 * Created by JAI on 15,November,2019
 * JAI KHAMBHAYTA
 */
class ChatListAdapter(var listener: ItemClickRecycler, var teamList: List<User>) : RecyclerView.Adapter<BaseViewHolder>() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val mMovieViewBinding = ItemChatListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TeamViewHolder(mMovieViewBinding)

    }

    override fun getItemCount(): Int = teamList.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class TeamViewHolder(val mMovieViewBinding: ItemChatListBinding?) :
        BaseViewHolder(mMovieViewBinding!!.root), ChatListItemViewModel.MovieItemViewModelListener {
        override fun onItemClick(user: User) {
            Log.d("TAG", "onItemClick: ")
            listener.addFriendToList(user)
        }

        private lateinit var teamItemViewModel: ChatListItemViewModel
        override fun onBind(position: Int) {
            val movie = teamList[position]
            teamItemViewModel = ChatListItemViewModel(movie, this)
            mMovieViewBinding!!.viewModel = teamItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mMovieViewBinding!!.executePendingBindings()
        }
    }
}


