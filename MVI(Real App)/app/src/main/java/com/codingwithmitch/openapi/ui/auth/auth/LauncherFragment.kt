package com.codingwithmitch.openapi.ui.auth.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.codingwithmitch.openapi.R
import kotlinx.android.synthetic.main.fragment_laucher.*

class LauncherFragment : BaseAuthFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_laucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            findNavController().navigate(R.id.action_laucherFragment_to_registerFragment)
        }

        login.setOnClickListener {
            findNavController().navigate(R.id.action_laucherFragment_to_loginFragment)
        }

        forgot_password.setOnClickListener {
            findNavController().navigate(R.id.action_laucherFragment_to_forgotPasswordFragment)
        }
    }
}
