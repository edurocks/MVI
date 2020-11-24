package com.joaquimley.heetch.heetchest.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * Demonstrates converting a [Drawable] to a [BitmapDescriptor],
 * for use as a marker icon.
 *
 * References:
 *   - https://stackoverflow.com/questions/33548447/vectordrawable-with-googlemap-bitmapdescriptor
 *
 *   - https://issuetracker.google.com/issues/35827905
 *
 *   - https://issuetracker.google.com/issues/35827905#comment23
 */
fun Drawable.toBitmapDescriptor(): BitmapDescriptor? {
    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}