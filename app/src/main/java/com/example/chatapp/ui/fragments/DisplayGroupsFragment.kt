package com.example.chatapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapp.databinding.FragmentGroupsBinding
import com.example.chatapp.ui.adapters.GroupsRecyclerViewAdapter

class DisplayGroupsFragment:Fragment() {

    private lateinit var binding:FragmentGroupsBinding
    private val groupsRecyclerViewAdapter:GroupsRecyclerViewAdapter= GroupsRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentGroupsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}