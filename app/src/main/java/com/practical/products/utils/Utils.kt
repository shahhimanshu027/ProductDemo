package com.practical.products.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Looper
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.practical.products.R

/**
 * class is used to declare methods inside an object or use package-level functions
 */

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

@BindingAdapter("imageUrl")
fun ShapeableImageView.setImageUrl(imgUrl: String?) {
    this.let { imageView ->
        imgUrl?.let {
            imageView.load(it) {
                placeholder(ColorDrawable(ContextCompat.getColor(imageView.context, R.color.color_view_line)))
            }
        }
    }
}

@BindingAdapter("price")
fun TextView.price(value: Int?) {
    value?.let {
        if (it.toString().isNotEmpty()) {
            this.text = this.context.getString(R.string.price_string, value)
        }
    }
}

@BindingAdapter("discount")
fun TextView.discount(value: Float?) {
    value?.let {
        if (it.toString().isNotEmpty()) {
            this.text = this.context.getString(R.string.discount_string, value)
        }
    }
}

@BindingAdapter("rating")
fun TextView.rating(value: Float?) {
    value?.let {
        if (it.toString().isNotEmpty()) {
            this.text = value.toString()
        }
    }
}

fun showToast(msg: String?, context: Context): Toast? {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        return null
    }
    if (!TextUtils.isEmpty(msg)) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.show()
        return toast
    }
    return null
}
