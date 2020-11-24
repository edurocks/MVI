package com.joaquimley.heetch.heetchest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joaquimley.heetch.heetchest.R
import kotlinx.android.synthetic.main.fragment_request_location_exaplainer.*

/**
 * Simple bottom sheet fragment that explains the user why we need to
 * request the Location permission.
 */
class RequestLocationExplainFragment : BottomSheetDialogFragment() {

    private var buttonListener: (() -> Unit)? = null

    override fun getTheme() = R.style.BottomSheetDialogTheme // Rounded corners

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        View.inflate(context, R.layout.fragment_request_location_exaplainer, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        action_btn.setOnClickListener {
            buttonListener?.invoke()
            dismiss()
        }
    }

    companion object {
        fun display(supportFragmentManager: FragmentManager, tag: String? = null, listener: () -> Unit)
                : RequestLocationExplainFragment {

            return RequestLocationExplainFragment().apply {
                buttonListener = listener
                show(supportFragmentManager, tag)
            }
        }
    }
}