package com.joaquimley.heetch.heetchest.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.joaquimley.heetch.heetchest.R
import com.joaquimley.heetch.heetchest.model.DriverUiModel
import kotlinx.android.synthetic.main.view_driver_info.view.*

class DriverInfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    CardView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_driver_info, this)
    }

    fun set(driver: DriverUiModel) {
        Glide.with(context)
            .load(driver.imageUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(driver_view_iv)

        driver_view_name_tv.text = driver.name
        driver_view_distance_tv.text = driver.distanceText
    }
}