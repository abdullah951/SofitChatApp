package com.sofit.test.ui.fragment.home.friendList

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.sofit.test.BR
import com.sofit.test.R
import com.sofit.test.data.model.User
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.FragmentChatListBinding
import com.sofit.test.databinding.FragmentFriendListBinding
import com.sofit.test.ui.activity.chatActivity.ChatActivity
import com.sofit.test.ui.base.BaseFragment
import com.sofit.test.ui.fragment.home.chatList.adapter.ChatListAdapter
import com.sofit.test.ui.fragment.home.friendList.adapter.FriendListAdapter
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup2.edtUsername
import kotlinx.android.synthetic.main.activity_signup2.spnCountry
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
@AndroidEntryPoint
class FriendListFragment : BaseFragment<FragmentFriendListBinding, FriendListViewModel>(),FriendListNavigator, ItemClickChat{
    @Inject
    lateinit var friendListViewModel: FriendListViewModel

    @Inject
    lateinit var sharedPreference: AppPreferences
    lateinit var fragmentFriendListBinding: FragmentFriendListBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_friend_list

    override fun getViewModel(): FriendListViewModel {return friendListViewModel}

    override fun getLifeCycleOwner(): LifecycleOwner = this

    private lateinit var auth: FirebaseAuth
    var list = ArrayList<User>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFriendListBinding = getViewDataBinding()
        friendListViewModel.setNavigator(this)
        setUp()
        allFriendList()
    }

    private fun setUp() {
        auth = Firebase.auth
    }

    fun allFriendList() {
        Log.d("TAG", "getUsersList: ")
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebase.uid).child("friends")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: "+error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)
                    Log.d("TAG", "onDataChange: "+user?.userId)
                    list.add(user!!)
                    Log.d("TAG", "onDataChange: "+list[0])
                }

                val friendListAdapter = FriendListAdapter(this@FriendListFragment, list)

                recyclerviewChatList.adapter = friendListAdapter
            }

        })
    }

    override fun openChat(user: User) {
        Log.d("TAG", "openChat: ")
        moveToChatActivity(user)
    }

}