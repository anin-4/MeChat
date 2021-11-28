package com.example.chatapp.domain.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val createdAt:String?="",
    val createdBy:String?="",
    val groupId:String?="",
    val name:String?="",
    var recentMessage:String?="",
    val timeOfLastMessage:String?="",

):Parcelable{
    companion object {
        fun DocumentSnapshot.toGroup(): Group {
            val createdAt = getString("createdAt")
            val createdBy = getString("CreatedBy")
            val groupId = getString("groupId")
            val name = getString("name")
            val recentMessage = getString("recentMessage")
            val timeOfLastMessage = getString("timeOfLastMessage")
            return Group(createdAt, createdBy, groupId, name, recentMessage, timeOfLastMessage)
        }
    }
}
