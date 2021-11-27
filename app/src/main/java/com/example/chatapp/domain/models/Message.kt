package com.example.chatapp.domain.models

import com.google.firebase.firestore.DocumentSnapshot

data class Message(
    val messageText:String?="",
    val sentBy:String?="",
    val sendAt:String?="",
    val senderName:String?="",
    val id:String?=""
){
    companion object{
        fun DocumentSnapshot.toMessage():Message{
            val messageText = getString("messageText")
            val sentBy = getString("sentBy")
            val sendAt = getString("sendAt")
            val senderName = getString("senderName")
            val id = getString("id")
            return Message(messageText,sentBy,sendAt,senderName,id)
        }
    }
}
