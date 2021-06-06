package com.gmail.uia059466.liska.setting.feedback

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.gmail.uia059466.liska.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EmptyFeedbackDialog : DialogFragment() {
  companion object {
    const val TAG = "EmptyFeedback"
  }
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.dialog_no_empty_feedback))
            .setPositiveButton(android.R.string.ok) { _, _ ->
              dismiss()
              
            }
    return builder.create()
  }
}