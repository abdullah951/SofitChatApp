package com.sofit.test.ui.fragment.home.chatFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sofit.test.data.model.Chat
import com.sofit.test.data.model.User
import com.sofit.test.databinding.ItemChatLeftBinding
import com.sofit.test.databinding.ItemChatListBinding
import com.sofit.test.databinding.ItemChatRightBinding
import com.sofit.test.ui.base.BaseViewHolder
import com.sofit.test.ui.fragment.home.chatList.ItemClickRecycler

class ChatAdapter(var chatList: List<Chat>) : RecyclerView.Adapter<BaseViewHolder>() {
    
    private val MESSAGE_TYPE_RIGHT = 0
    private val MESSAGE_TYPE_LEFT = 1
    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if(viewType == MESSAGE_TYPE_LEFT) {
            val mMovieViewBinding = ItemChatLeftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            return ChatLeftViewHolder(mMovieViewBinding)
        } else {
            val mMovieViewBinding = ItemChatRightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            return ChatRightViewHolder(mMovieViewBinding)
        }



    }

    override fun getItemCount(): Int = chatList.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ChatLeftViewHolder(val mMovieViewBinding: ItemChatLeftBinding?) :
        BaseViewHolder(mMovieViewBinding!!.root), ChatItemViewModel.MovieItemViewModelListener {
        override fun onItemClick(user: Chat) {
            Log.d("TAG", "onItemClick: ")

        }

        private lateinit var teamItemViewModel: ChatItemViewModel
        override fun onBind(position: Int) {
            val movie = chatList[position]
            teamItemViewModel = ChatItemViewModel(movie, this)
            mMovieViewBinding!!.viewModel = teamItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mMovieViewBinding!!.executePendingBindings()
        }
    }

    inner class ChatRightViewHolder(val mMovieViewBinding: ItemChatRightBinding?) :
        BaseViewHolder(mMovieViewBinding!!.root), ChatItemViewModel.MovieItemViewModelListener {
        override fun onItemClick(user: Chat) {
            Log.d("TAG", "onItemClick: ")

        }

        private lateinit var teamItemViewModel: ChatItemViewModel
        override fun onBind(position: Int) {
            val movie = chatList[position]
            teamItemViewModel = ChatItemViewModel(movie, this)
            mMovieViewBinding!!.viewModel = teamItemViewModel
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mMovieViewBinding!!.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }

}


