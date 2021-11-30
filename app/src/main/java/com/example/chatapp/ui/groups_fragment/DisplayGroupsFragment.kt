package com.example.chatapp.ui.groups_fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentGroupsBinding
import com.example.chatapp.ui.groups_fragment.adapters.GroupsRecyclerViewAdapter
import com.example.chatapp.ui.groups_fragment.addGroupDialog.AddGroupButtonListener
import com.example.chatapp.ui.groups_fragment.addGroupDialog.AddGroupDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DisplayGroupsFragment:Fragment() {

    private lateinit var binding:FragmentGroupsBinding
    private val groupsRecyclerViewAdapter:GroupsRecyclerViewAdapter= GroupsRecyclerViewAdapter()
    private val groupsViewModel: GroupsViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentGroupsBinding.inflate(inflater)
        auth= FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser==null){
            findNavController().navigate(R.id.action_fragmentGroups_to_loginFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutOptionMenu -> {
                auth.signOut()
                findNavController().navigate(R.id.action_fragmentGroups_to_loginFragment)
            }
        }
        return true
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply{
            groupsRecyclerView.adapter=groupsRecyclerViewAdapter
            groupsRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        }

        groupsViewModel.groups.observe(viewLifecycleOwner,{
            groupsRecyclerViewAdapter.submitList(it)
        })

        binding.addGroup.setOnClickListener {
            AddGroupDialog(requireContext(),object : AddGroupButtonListener{
                override fun onAddButtonListener(name: String) {
                    groupsViewModel.addNewGroup(name)
                }
            }).show()
        }

        groupsRecyclerViewAdapter.onClickHandler={
            val bundle=Bundle().apply {
                putParcelable("group",it)
                putString("labelName",it.name)
            }
            findNavController().navigate(R.id.action_fragmentGroups_to_chatFragment,bundle)
        }

    }
}