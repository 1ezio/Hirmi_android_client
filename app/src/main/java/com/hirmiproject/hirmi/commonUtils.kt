package com.hirmiproject.hirmi

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable


public class    commonUtils {
    fun showLoadingDialog(context: Context):Dialog{
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setContentView(R.layout.progress)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
    fun dismiss(context: Context) {
        val progress = Dialog(context)
        progress.dismiss()
        return
    }

}