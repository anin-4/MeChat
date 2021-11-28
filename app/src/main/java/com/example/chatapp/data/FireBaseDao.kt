package com.example.chatapp.data

import android.util.Log
import com.example.chatapp.domain.models.Group
import com.example.chatapp.domain.models.Group.Companion.toGroup
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.models.Message.Companion.toMessage
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

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


    suspend fun loadInitialChat(groupId:String):List<Message>{
        val groupMessage =db.collection("Message").document(groupId).collection("messages")
        val list = mutableListOf<Message>()
        try{
            val hold =groupMessage
                .orderBy("sendAt")
                .get()
                .await()

            for(document in hold.documents){
                document.toObject(Message::class.java)?.let { list.add(it) }
            }
        }catch (e:Exception){
            Log.d(TAG, "loadInitialChat: error")
        }

        return list
    }

    suspend fun postMessage(groupId:String,messageTextNew:String){
        val messageId = db.collection("Message").document(groupId).collection("messages").document().id

        val newMessage = Message(
            messageText = messageTextNew,
            sentBy = Firebase.auth.currentUser?.uid,
            sendAt = getCurrentDateTime().toString(),
            senderName = Firebase.auth.currentUser?.displayName,
            id = messageId
        )

        db.collection("Message").document(groupId).collection("messages").document(messageId).set(newMessage)


    }

    @ExperimentalCoroutinesApi
    fun listenChat(groupId:String):Flow<List<Message>?>{
        return callbackFlow {
                    val listener = db
                        .collection("Message")
                        .document(groupId)
                        .collection("messages")
                        .orderBy("sendAt")
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Log.d(TAG, "listenChat: error!")
                                return@addSnapshotListener
                            }
                            val map = value?.documents?.map {
                                it.toMessage()
                            }
                            var finalMessage:String?=""
                            finalMessage = try {
                                map?.last()?.messageText
                            }catch (e:Exception){
                                "";
                            }
                            updateGroupFinalMessage(finalMessage,groupId)

                            trySend(map).isSuccess
                        }

                    awaitClose {
                        listener.remove()
                    }
                }
    }

    private fun updateGroupFinalMessage(finalMessage: String?="",groupId: String){
        db.collection("Groups").document(groupId).update("recentMessage",finalMessage)
    }
}