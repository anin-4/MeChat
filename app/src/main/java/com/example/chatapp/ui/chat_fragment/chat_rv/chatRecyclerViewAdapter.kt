package com.example.chatapp.ui.chat_fragment.chat_rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.R
import com.example.chatapp.databinding.ReceivedTextItemBinding
import com.example.chatapp.databinding.SendTextItemBinding
import com.example.chatapp.domain.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatRecyclerViewAdapter:ListAdapter<Message,ChatRecyclerViewHolder>(ChatDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRecyclerViewHolder {
        return when(viewType){
            R.layout.received_text_item->ChatRecyclerViewHolder
                .ReceivedText(
                    ReceivedTextItemBinding
                    .inflate(LayoutInflater.from(parent.context),parent,false)
                )
            R.layout.send_text_item->ChatRecyclerViewHolder
                .SendText(
                    SendTextItemBinding
                        .inflate(LayoutInflater.from(parent.context),parent,false)
                )
            else -> throw IllegalArgumentException("Illegal View type")
        }
    }

    override fun onBindViewHolder(holder: ChatRecyclerViewHolder, position: Int) {
        when(holder){
            is ChatRecyclerViewHolder.SendText-> holder.bind(getItem(position))
            is ChatRecyclerViewHolder.ReceivedText -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message= getItem(position)
        if(message.sentBy==Firebase.auth.currentUser?.uid){
            return R.layout.send_text_item
        }
        return R.layout.received_text_item
    }

    class ChatDiffUtil:DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem==newItem
        }

    }
}