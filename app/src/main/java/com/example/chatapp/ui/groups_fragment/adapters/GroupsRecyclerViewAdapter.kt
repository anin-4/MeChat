package com.example.chatapp.ui.groups_fragment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.databinding.ItemGroupBinding
import com.example.chatapp.domain.models.Group

class GroupsRecyclerViewAdapter:ListAdapter<Group,GroupsRecyclerViewHolder>(DiffCallback()) {

    var onClickHandler:((item:Group)->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsRecyclerViewHolder {
            val binding =ItemGroupBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return GroupsRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsRecyclerViewHolder, position: Int) {
        val item=this.getItem(position)
        holder.onClickHandler=onClickHandler
        holder.bind(item)
    }

    class DiffCallback:DiffUtil.ItemCallback<Group>(){
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.groupId==newItem.groupId
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem==newItem
        }

    }
}