package com.example.chatapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.chatapp.data.FireBaseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi

class GroupsViewModel:ViewModel() {

    private val fireBaseDao =FireBaseDao()

    @ExperimentalCoroutinesApi
    val groups = fireBaseDao.getGroups().asLiveData()

}