package com.example.travello.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.travello.Data.Repository.AuthRepository
import com.example.travello.R
import com.example.travello.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    lateinit private var mBinding : FragmentSignInBinding
    lateinit private var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}

