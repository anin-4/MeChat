package com.example.chatapp.ui.chat_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.ui.chat_fragment.chat_rv.ChatRecyclerViewAdapter
import com.example.chatapp.utils.Constants.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ChatFragment:Fragment() {

    private lateinit var binding:FragmentChatBinding
    private val args:ChatFragmentArgs by navArgs()
    private lateinit var viewModel:ChatViewModel
    private lateinit var viewModelFactory: ChatViewModelFactory
    private val chatRecyclerViewAdapter:ChatRecyclerViewAdapter = ChatRecyclerViewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentChatBinding.inflate(inflater)

        args.group?.groupId?.let {
            viewModelFactory = ChatViewModelFactory(it)
        }

        viewModel= ViewModelProvider(this,viewModelFactory)[ChatViewModel::class.java]

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val gid = args.group?.groupId
//        chatViewModel.groupId=gid

        binding.ChatsRecyclerView.apply {
            adapter=chatRecyclerViewAdapter
            layoutManager=LinearLayoutManager(requireContext())
        }

        viewModel.loadInitialChat.observe(viewLifecycleOwner){
            chatRecyclerViewAdapter.submitList(it)
        }

        viewModel.chats.observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: $it")
            chatRecyclerViewAdapter.submitList(it)
        }

        binding.sendMessageButton.setOnClickListener {
           val messageText= binding.sendMessageEditText.text.toString()
            viewModel.putData(messageText)
        }

    }
}