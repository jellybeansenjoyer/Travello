package com.example.travello.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.travello.R
import com.example.travello.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    lateinit private var mBinding : FragmentSignInBinding
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
        mBinding.loginTextClickable.setOnClickListener {
            (activity as MainActivity).switchActivity(SignUpFragment(),false);
        }
    }
}

