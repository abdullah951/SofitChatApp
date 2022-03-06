package com.sofit.test.ui.fragment.home.chatList

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
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
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
import com.sofit.test.databinding.FragmentProfileBinding
import com.sofit.test.ui.activity.main.MainActivity
import com.sofit.test.ui.base.BaseFragment
import com.sofit.test.ui.fragment.home.chatList.adapter.ChatListAdapter
import com.sofit.test.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_signup2.edtUsername
import kotlinx.android.synthetic.main.activity_signup2.spnCountry
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding, ChatListViewModel>(),ChatListNavigator, ItemClickRecycler{
    @Inject
    lateinit var chatListViewModel: ChatListViewModel

    @Inject
    lateinit var sharedPreference: AppPreferences
    lateinit var fragmentChatListBinding: FragmentChatListBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_chat_list

    override fun getViewModel(): ChatListViewModel {return chatListViewModel}

    override fun getLifeCycleOwner(): LifecycleOwner = this

    private lateinit var auth: FirebaseAuth
    var list = ArrayList<User>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentChatListBinding = getViewDataBinding()
        chatListViewModel.setNavigator(this)
        setUp()
    }

    private fun setUp() {
        auth = Firebase.auth
        chatListViewModel.viewModelScope.launch(Dispatchers.Main) {
            if (list.isEmpty()) {
                showLoading()
                val data = chatListViewModel.fetchAllUserRemote()
                if (data.first == 1) {
                    showMessage(data.second)
                }
                hideLoading()
            }

        }

        // load data from local
        chatListViewModel.viewModelScope.launch {
            chatListViewModel.fetchDataFromDatabase().observe(viewLifecycleOwner, Observer {
                list = it as ArrayList<User>
                if (list.isEmpty()) setEmptyView(true) else setEmptyView()
                val userAdapter = ChatListAdapter(this@ChatListFragment, list)

                recyclerviewChatList.adapter = userAdapter

            })
        }
    }

    override fun alllist(): ArrayList<User> {
        Log.d("TAG", "getUsersList: ")
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: "+error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)
                    Log.d("TAG", "onDataChange: "+user?.userId)
                    if (user!!.userId != firebase.uid) {

                        list.add(user)
                        Log.d("TAG", "onDataChange: "+list.toArray())
                    }
                }

                val userAdapter = ChatListAdapter(this@ChatListFragment, list)

                recyclerviewChatList.adapter = userAdapter
                chatListViewModel.viewModelScope.launch(Dispatchers.Main) {
                    chatListViewModel.insertAllUserDatabase(list)
                }
            }

        })
        return list
    }

    override fun addFriendToList(friendToAdd: User) {
        Log.d("TAG", "addFriendToList: ")
        val user: FirebaseUser? = auth.currentUser
        val userId:String = user!!.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("friends").child(friendToAdd.userId)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["userId"] = friendToAdd.userId
        hashMap["userName"] = friendToAdd.userName
        hashMap["country"] = friendToAdd.country
        hashMap["profileImage"] = friendToAdd.profileImage
        databaseReference.setValue(hashMap).addOnCompleteListener(requireActivity()){
            if (it.isSuccessful){
                Toast.makeText(requireContext(), "Friend Added", Toast.LENGTH_SHORT).show()
            }
        }
    }

}