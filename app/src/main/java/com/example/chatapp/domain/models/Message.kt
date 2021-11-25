package com.example.chatapp.domain.models

data class Message(
    val messageText:String?="",
    val sentBy:String?="",
    val sendAt:String?=""
)
