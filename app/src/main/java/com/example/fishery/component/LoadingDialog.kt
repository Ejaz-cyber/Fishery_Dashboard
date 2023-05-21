package com.example.fishery.component

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.example.fishery.R

class LoadingDialog(activity: Activity) {

    private var alertDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.sample_loading, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
    }

    fun startLoading() {
        alertDialog.show()
    }

    fun stopLoading() {
        alertDialog.dismiss()
    }
}