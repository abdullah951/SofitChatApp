package com.sofit.test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ResponseChat(
    val data: MutableList<Chat>
)

@Entity(tableName = "chat")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= 0,
    var senderId:String = "",
    var receiverId:String = "",
    var message:String = ""
)