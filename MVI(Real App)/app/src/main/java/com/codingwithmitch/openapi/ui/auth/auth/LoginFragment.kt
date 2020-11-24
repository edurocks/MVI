package com.codingwithmitch.openapi.ui.auth.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.auth.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.input_email
import kotlinx.android.synthetic.main.fragment_login.input_password
import kotlinx.android.synthetic.main.fragment_register.*

class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        login_button.setOnClickListener { login() }
    }

    private fun subscribeObservers(){
        viewModel._viewState.observe(viewLifecycleOwner, Observer { authViewState ->
            authViewState.loginFields?.let { loginFields ->

                loginFields.login_email?.let {
                    input_email.setText(it)
                }

                loginFields.login_password?.let {
                    input_password.setText(it)
                }
            }
        })
    }

    fun login(){
        viewModel.setStateEvent(AuthStateEvent.LoginAttemptEvent(
            input_email.text.toString(),
            input_password.text.toString()
        ))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(LoginFields(input_email.text.toString(), input_password.text.toString()))
    }

}
