package com.upmyranksapp.helper

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.upmyranksapp.R


class MyProgressBar(val activity: Activity) {
    private var alertDialog: AlertDialog? = null
    fun show(){
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity,R.style.DialogTheme)
// ...Irrelevant code for customizing the buttons and title
// ...Irrelevant code for customizing the buttons and title
        val inflater: LayoutInflater = activity.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.default_placeholder_progress, null)
        dialogBuilder.setView(dialogView)

        val editText = dialogView.findViewById<View>(R.id.state_text) as TextView
        editText.text = "loading.."
        alertDialog = dialogBuilder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun dismiss(){
        if(alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

}