package com.gmail.uia059466.liska.utils

import android.content.*
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.gmail.uia059466.liska.R

object BrowserUtils {
    fun isBrowserInstalled(context: Context): Boolean {
        val siteUrl=context.getString(R.string.liska_url)
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl))
        return intent.resolveActivity(context.packageManager) != null
    }

    
    fun showDialogErrorBrowser(base: Context) {
        AlertDialog.Builder(base)
            .setTitle(R.string.dialog_title_error_browser)
            .setMessage(R.string.error_browser)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}