package com.sofit.test.ui.activity.chatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.sofit.test.data.model.Chat
import com.sofit.test.data.model.User
import com.sofit.test.data.source.preference.AppPreferences
import com.sofit.test.databinding.ActivitySignupBinding
import com.sofit.test.ui.base.BaseActivity
import com.sofit.test.ui.fragment.home.chatFragment.adapter.ChatAdapter
import com.sofit.test.ui.fragment.home.chatList.adapter.ChatListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.recyclerviewChatList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivitySignupBinding, ChatViewModel>(),ChatNavigator  {

    @Inject
    lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var sharedPreference: AppPreferences

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_chat

    override fun getViewModel(): ChatViewModel = chatViewModel

    private lateinit var auth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null
    var list = ArrayList<Chat>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        chatViewModel.setNavigator(this)
        // Initialize Firebase Auth
        auth = Firebase.auth
        firebaseUser = FirebaseAuth.getInstance().currentUser
        setUp()

    }

    private fun setUp() {
        auth = Firebase.auth
        val extras = this.intent.extras
        val userId = extras!!.getString("friendId")
        val friendName = extras!!.getString("friendName")
        val profilePic = extras.getString("profilePic")
        friendNameText.text = friendName
        Glide.with(baseContext).load(profilePic).into(imgProfile)
        readMessage(firebaseUser!!.uid, userId!!)

        // load data from local
        chatViewModel.viewModelScope.launch {
            chatViewModel.fetchDataFromDatabase().observe(this@ChatActivity, Observer {
                list = it as ArrayList<Chat>
                val userAdapter = ChatAdapter(list)

                recyclerviewChatList.adapter = userAdapter

            })
        }

        btnSendMessage.setOnClickListener {
            var message: String = edtMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "Message is empty", Toast.LENGTH_SHORT).show()
                edtMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                edtMessage.setText("")
            }
        }
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message

        reference!!.child("Chat").push().setValue(hashMap)
//        chatViewModel.viewModelScope.launch(Dispatchers.Main) {
//            chatViewModel.insertChatDatabase(list)
//        }

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId == senderId && chat!!.receiverId == receiverId ||
                        chat!!.senderId == receiverId && chat!!.receiverId == senderId
                    ) {
                        list.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(list)

                recyclerviewChatList.adapter = chatAdapter
                Log.d(TAG, "onDataChange: "+list.toString())
//                chatViewModel.viewModelScope.launch(Dispatchers.Main) {
//                    chatViewModel.insertChatDatabase(list)
//                }
            }
        })
    }

    companion object {
        const val TAG = "SignupActivity"
    }
}