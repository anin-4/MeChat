package com.example.chatapp.data

import android.util.Log
import com.example.chatapp.domain.models.Group
import com.example.chatapp.domain.models.Group.Companion.toGroup
import com.example.chatapp.domain.models.User
import com.example.chatapp.utils.Constants.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FireBaseDao {

    private val db = Firebase.firestore

    suspend fun addUserIntoFireStore(user:User){
        db.collection("User").document(user.uid).set(user)
    }

    suspend fun createGroup(group:Group){
        val id = db.collection("Groups").document().id
        db.collection("Groups").document(id).set(group)
    }

    @ExperimentalCoroutinesApi
    fun getGroups(): Flow<List<Group>?> {
        return callbackFlow {
            val listener = db.collection("Groups").addSnapshotListener { value, error ->
                if(error!=null){
                    Log.d(TAG, "getGroups: error occurred ")
                    return@addSnapshotListener
                }
                val map = value?.documents?.map {
                    it.toGroup()
                }
                this.trySend(map).isSuccess
            }
            awaitClose {
                listener.remove()
            }
        }
    }
}