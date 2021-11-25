package com.example.chatapp.domain.models

import com.google.firebase.firestore.DocumentSnapshot

data class Group(
    val createdAt:String?="",
    val createdBy:String?="",
    val groupId:String?="",
    val name:String?="",
    val recentMessage:String?="",
    val timeOfLastMessage:String?="",

){
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
