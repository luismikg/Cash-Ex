package com.experiment.exchangerate.Helper

import com.experiment.exchangerate.R
import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget

class ViewDialog( acti: Activity) {

    private var dialog: Dialog? = null
    private var act: Activity? = null

    init {
        act = acti
    }

    fun showDialog() {

        dialog = Dialog(act!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.custom_loading_layout)

        val gifImageView = dialog!!.findViewById<ImageView>(R.id.custom_loading_imageView)

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)

        Glide.with(act)
            .load(R.drawable.loading)
            .placeholder(R.drawable.loading)
            .centerCrop()
            .crossFade()
            .into(imageViewTarget)
        dialog!!.show()
    }

    fun hideDialog() {
        dialog!!.dismiss()
    }

}