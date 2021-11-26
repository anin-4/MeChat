package com.example.chatapp.data

import android.util.Log
import com.example.chatapp.domain.models.Group
import com.example.chatapp.domain.models.Group.Companion.toGroup
import com.example.chatapp.domain.models.User
import com.example.chatapp.utils.Constants.TAG
import com.example.chatapp.utils.getCurrentDateTime
import com.google.firebase.auth.ktx.auth
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

    suspend fun createGroup(groupName:String){
        val id = db.collection("Groups").document().id
        val newGroup = Group(
            createdAt = getCurrentDateTime().toString(),
            createdBy = Firebase.auth.currentUser?.uid,
            groupId = id,
            name =groupName,
            recentMessage = "",
            timeOfLastMessage = ""
        )
        db.collection("Groups").document(id).set(newGroup)
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