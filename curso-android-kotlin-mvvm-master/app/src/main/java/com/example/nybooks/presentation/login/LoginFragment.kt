package com.example.nybooks.presentation.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.auth0.android.jwt.JWT
import com.example.nybooks.R
import com.example.nybooks.data.repository.BooksApiDataSource
import com.example.nybooks.presentation.books.BooksActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    private lateinit var loginViewModel : LoginViewModel
    private lateinit var alertDialog : AlertDialog
    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var login : MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        login = view.findViewById(R.id.login)

        loginViewModel = LoginViewModel.ViewModelFactory(BooksApiDataSource(activity!!.application)).create(LoginViewModel::class.java)

        loginViewModel.getUserData(username.text.toString())
        loginViewModel.userData.observe(this, Observer { userData ->
            if (userData != null){
                startActivity(Intent(activity, BooksActivity::class.java))
            }
        })

        login.setOnClickListener {
            generateLoginDialog()
            loginViewModel.getToken(username.text.toString(), password.text.toString())
        }

        loginViewModel.token.observe(this, Observer { token ->
            loginViewModel.loginUser(getDecodedJwtTokenDeviceId(token)!!, token!!)
        })

        loginViewModel.loginResponse.observe(this, Observer { loginResponse ->
            if (loginResponse != null){
                alertDialog.dismiss()
                startActivity(Intent(activity, BooksActivity::class.java))
            }
        })
    }

    private fun generateLoginDialog() {
        val alertDialogBuilder = AlertDialog.Builder(context!!)
        alertDialogBuilder.setTitle("Autenticando...")
        alertDialogBuilder.setMessage("Vamos autenticar...")
        alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getDecodedJwtTokenDeviceId(token: String?): String? {
        val jwt = JWT(token!!)
        return jwt.subject
    }
}
