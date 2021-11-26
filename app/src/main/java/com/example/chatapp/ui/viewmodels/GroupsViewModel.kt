package com.example.chatapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.FireBaseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class GroupsViewModel:ViewModel() {

    private val fireBaseDao =FireBaseDao()

    @ExperimentalCoroutinesApi
    val groups = fireBaseDao.getGroups().asLiveData()


    fun addNewGroup(name:String)= viewModelScope.launch {
        fireBaseDao.createGroup(name)
    }

}