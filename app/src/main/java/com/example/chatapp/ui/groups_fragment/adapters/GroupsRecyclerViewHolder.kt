package com.example.chatapp.ui.groups_fragment.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemGroupBinding
import com.example.chatapp.domain.models.Group

class GroupsRecyclerViewHolder(private val binding:ItemGroupBinding):RecyclerView.ViewHolder(binding.root) {

    var onClickHandler:((item:Group)->Unit)?=null

    fun bind(item:Group){
        binding.apply {
            groupNameTextView.text=item.name
            latestTime.text=item.timeOfLastMessage
            recentMessage.text=item.recentMessage
            root.setOnClickListener {
                onClickHandler?.invoke(item)
            }
        }
    }
}