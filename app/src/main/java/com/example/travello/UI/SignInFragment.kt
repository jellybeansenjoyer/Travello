package com.example.travello.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.travello.Data.Repository.AuthRepository
import com.example.travello.R
import com.example.travello.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignInFragment : Fragment() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int =1
    lateinit private var mBinding : FragmentSignInBinding
    lateinit private var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun googleInit(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

         mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sign_in, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRepository = AuthRepository(requireContext())

        googleInit()

        mBinding.loginTextClickable.setOnClickListener {
            (activity as MainActivity).switchActivity(SignUpFragment(),false);
        }

        mBinding.signInButton.setOnClickListener {
            val email = mBinding.email.text.toString()
            val password = mBinding.password.text.toString()
            if(!email.isNullOrBlank() && !email.isNullOrEmpty() && !password.isNullOrEmpty() && !password.isNullOrBlank()) {
                authRepository.login(email, password) { token ->
                    if (token != null) {
                        (activity as MainActivity).switchActivity(TemporarySuccessFragment(), false)
                    } else {
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(), "Email or Password empty", Toast.LENGTH_SHORT).show()
            }
        }
        
        mBinding.google.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            this@SignInFragment.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken
            val email = account.email
            val displayName = account.displayName
            Log.e("Google Sign-In","IdToken: "+idToken+" Email: "+email+" DisplayName: "+displayName);
            (activity as MainActivity).switchActivity(TemporarySuccessFragment(),false);
//        } catch (e: ApiException) {
//            // Handle sign in failure
//            Log.e("Google Sign-In",e.toString())
//            Log.e("Google Sign-In", "signInResult:failed code=" + e.statusCode)
//        }
    }

}

