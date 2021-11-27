package com.example.chatapp.ui.chat_fragment

import android.util.Log
import androidx.lifecycle.*
import com.example.chatapp.data.FireBaseDao
import com.example.chatapp.domain.models.Message
import com.example.chatapp.utils.Constants.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class ChatViewModel:ViewModel() {

    private val fireBaseDao:FireBaseDao = FireBaseDao()

    private var _loadInitialChat = MutableLiveData<List<Message>>()
    val loadInitialChat:LiveData<List<Message>>
    get() = _loadInitialChat

    var groupId:String =""

    init {
        groupId?.let {
        viewModelScope.launch {
            _loadInitialChat.postValue(fireBaseDao.loadInitialChat(it))
            Log.d(TAG, "viewModel: ${_loadInitialChat.value}")
        }
        }
    }

    fun putData(message:String)=viewModelScope.launch {
        groupId?.let {
            fireBaseDao.postMessage(it, message)
        }

    }

    @ExperimentalCoroutinesApi
    val chats = fireBaseDao.listenChat(groupId).asLiveData()
}