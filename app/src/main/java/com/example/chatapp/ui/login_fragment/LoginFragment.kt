package com.example.chatapp.ui.login_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chatapp.BuildConfig
import com.example.chatapp.R
import com.example.chatapp.data.FireBaseDao
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.domain.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginFragment:Fragment() {

    private lateinit var binding:FragmentLoginBinding

    private lateinit var auth:FirebaseAuth

    private var fireBaseDao: FireBaseDao = FireBaseDao()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLoginBinding.inflate(inflater)
        auth= FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let{
            findNavController().navigate(R.id.action_loginFragment_to_fragmentGroups)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.signInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("your key")
                .requestEmail()
                .requestId()
                .requestProfile()
                .build()

            val signInClient = GoogleSignIn.getClient(requireContext(), gso)

            signInClient.signInIntent.also {
                startActivityForResult(it, 124)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==124){
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let{
                googleAuthForFireBase(it)
            }
        }
    }

    private fun googleAuthForFireBase(googleSignInAccount: GoogleSignInAccount){
        val credentials=GoogleAuthProvider.getCredential(googleSignInAccount.idToken,null)

        lifecycleScope.launch {
            try{
                val newUser = auth.signInWithCredential(credentials).await()
                val fireBaseUser = newUser.user
                fireBaseUser?.let{
                    fireBaseDao.addUserIntoFireStore(
                        User(
                            uid = fireBaseUser.uid,
                            profilePicUrl = fireBaseUser.photoUrl.toString(),
                            username = fireBaseUser.displayName
                        )
                    )
                }
                withContext(Dispatchers.Main){
                    Snackbar.make(requireView(),"Signed In",Snackbar.LENGTH_LONG).show()
                }
                findNavController().navigate(R.id.action_loginFragment_to_fragmentGroups)
            }catch(e:Exception){
                withContext(Dispatchers.Main){
                    Snackbar.make(requireView(),e.message.toString(),Snackbar.LENGTH_LONG).show()
                }
        }
        }
    }
}