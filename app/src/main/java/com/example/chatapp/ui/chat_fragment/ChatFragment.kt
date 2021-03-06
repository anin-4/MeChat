package com.example.chatapp.ui.chat_fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
            binding.ChatsRecyclerView.scrollToPosition(chatRecyclerViewAdapter.itemCount-1)

        }

        binding.sendMessageButton.setOnClickListener {
           val messageText= binding.sendMessageEditText.text.toString()
            binding.sendMessageEditText.setText("")
            viewModel.putData(messageText)
        }

    }
}