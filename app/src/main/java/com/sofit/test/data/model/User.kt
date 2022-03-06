package com.sofit.test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ResponseUser(
    val data: MutableList<Player>
)

@Entity(tableName = "alluser")
data class User(
    @PrimaryKey
    var id: Int = 0,
    var userId:String = "",
    var userName:String = "",
    var country:String = "",
    var profileImage:String = ""
)
