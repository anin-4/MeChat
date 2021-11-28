package com.example.chatapp.ui.chat_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ChatViewModelFactory(private val groupId: String):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(groupId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}