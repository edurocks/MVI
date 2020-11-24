package com.codingwithmitch.openapi.ui.auth


import android.util.Log
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.auth.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(), DataStateChangeListener{

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            GlobalScope.launch(Main) {
                displayProgressBar(it.loading.isLoading)

                it.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }

                it.data?.response?.let { responseEvent ->
                    handleStateResponse(responseEvent)
                }
            }
        }
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when(it.response.responseType){
                is  ResponseType.Toast ->{
                    it.response.message?.let { message ->
                       Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
                is  ResponseType.Dialog ->{
                    it.response.message?.let { message ->
                        MaterialDialog(this)
                            .show{
                                title(R.string.text_error)
                                message(text = message)
                                positiveButton(R.string.text_ok)
                            }
                    }
                }
                is  ResponseType.None ->{
                    Log.e("handleStateError:", "${it.response.message}")
                }
            }
        }
    }

    private fun handleStateResponse(responseEvent: Event<Response>) {
        responseEvent.getContentIfNotHandled()?.let{

            when(it.responseType){
                is ResponseType.Toast ->{
                    it.message?.let{message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is ResponseType.Dialog ->{
                    it.message?.let{ message ->
                        MaterialDialog(this)
                            .show{
                                title(R.string.text_success)
                                message(text = message)
                                positiveButton(R.string.text_ok)
                            }
                    }
                }

                is ResponseType.None -> {
                    Log.i("handleStateResponse", "handleStateResponse: ${it.message}")
                }
            }

        }
    }


    abstract fun displayProgressBar(boolean: Boolean)

}