package com.gmail.uia059466.liska.setting.rateus

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gmail.uia059466.liska.R

class RateUsDialog : DialogFragment() {
    var onOk: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title        =resources.getString(R.string.rate_us_dialog_title)
        val message      =resources.getString(R.string.rate_us_dialog_message)
        val rateUsLabel  =resources.getString(R.string.rate_us_dialog_btn_rate_us_label)
        val cancelLabel  =resources.getString(R.string.rate_us_dialog_btn_cancel_label)

        val view = requireActivity().layoutInflater.inflate(
                R.layout.rate_us_dialog_fragment, null)
        val builder = AlertDialog.Builder(requireActivity())
            .setView           (view)
            .setTitle          (title)
            .setMessage        (message)
            .setPositiveButton (rateUsLabel)    { _, _  -> showRating() }
            .setNeutralButton  (cancelLabel)    { _, _  -> dismiss(); onOk?.invoke()    }
        return builder.create()
    }


    private fun showRating() {
        try {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + requireActivity().packageName)
                    )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                    )
            )
        }
    }
}