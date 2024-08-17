package com.example.travello.UI

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
import com.example.travello.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    lateinit private var authRepository: AuthRepository
    lateinit private var mBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return mBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authRepository = AuthRepository(this.requireContext())
        mBinding.signUpTextClickable.isClickable = true;
        mBinding.signUpTextClickable.setOnClickListener {
            (activity as MainActivity).switchActivity(SignInFragment(), false);
        }
        mBinding.signUpButton.setOnClickListener {
            val email = mBinding.email.text.toString()
            val password = mBinding.password.text.toString()
            val name = mBinding.Name.text.toString()
            val confirmPassword = mBinding.confirmPassword.text.toString()
            if (!email.isNullOrBlank() && !email.isNullOrEmpty() && !password.isNullOrEmpty() && !password.isNullOrBlank()
                && !confirmPassword.isNullOrEmpty() && !confirmPassword.isNullOrBlank() && !name.isNullOrBlank() && !name.isNullOrEmpty()
                ) {
                if(confirmPassword.equals(password)){
                    authRepository.signup(name, email, password) { token ->
                        if (token != null) {
                            // Login successful, proceed to the next screen
                            (activity as MainActivity).switchActivity(
                                TemporarySuccessFragment(),
                                false
                            );

                        } else {
                            // Show error message
                            Toast.makeText(requireContext(), "Sign up failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Either of Fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}