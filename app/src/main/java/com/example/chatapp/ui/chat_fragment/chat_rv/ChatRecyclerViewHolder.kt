package com.example.chatapp.ui.chat_fragment.chat_rv


import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.chatapp.databinding.ReceivedTextItemBinding
import com.example.chatapp.databinding.SendTextItemBinding
import com.example.chatapp.domain.models.Message
import com.example.chatapp.utils.toString

sealed class ChatRecyclerViewHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root) {

    class ReceivedText(private val binding:ReceivedTextItemBinding):ChatRecyclerViewHolder(binding){
            fun bind(message:Message){
                binding.apply {
                    nameTagTextView.text=message.senderName
                    textMessageIncoming.text=message.messageText
                    timeTextView.text=message.sendAt?.toDate()?.toString("yyyy/MM/dd HH:mm:ss")
                }
            }
    }

    class SendText(private val binding:SendTextItemBinding):ChatRecyclerViewHolder(binding){
        fun bind(message: Message){
            binding.apply {
                textMessageOutgoing.text=message.messageText
                timeTextViewSend.text=message.sendAt?.toDate()?.toString("yyyy/MM/dd HH:mm:ss")
            }
        }
    }
}