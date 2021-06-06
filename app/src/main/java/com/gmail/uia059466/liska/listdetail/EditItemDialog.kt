package com.gmail.uia059466.liska.listdetail

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gmail.uia059466.liska.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditItemDialog : DialogFragment() {
  
  companion object {
    private const val EXTRA_TITLE = "title"
    private const val EXTRA_TEXT = "text"
    
    fun newInstance(title: String, text: String): EditItemDialog {
      val dialog = EditItemDialog()
      val args = Bundle().apply {
        putString(EXTRA_TITLE, title)
        putString(EXTRA_TEXT, text)
        
        
      }
      dialog.arguments = args
      return dialog
    }
  }
  
  lateinit var editText: EditText
  var onOk: (() -> Unit)? = null
  var onCancel: (() -> Unit)? = null
  
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val title = arguments?.getString(EXTRA_TITLE)
    val text: String? = arguments?.getString(EXTRA_TEXT)
    val view = activity?.layoutInflater?.inflate(R.layout.dialog_edit_word, null)
    editText = view!!.findViewById(R.id.editText)
    val textLength = 50
    editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(textLength))
    if (text != null) {
      val textTrim = text.trim()
      editText.setText(textTrim)
      editText.setSelection(textTrim.length)
    }
    
    editText.addTextChangedListener(sendTextEndIconTextWatcher)
    editText.requestFocus()
    
    val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
              onOk?.invoke()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
              onCancel?.invoke()
            }
    dialog = builder.create()
    
    
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    return dialog
  }
  
  lateinit var positiveButton: Button
  lateinit var dialog: AlertDialog
  
  override fun onStart() {
    super.onStart()
    positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
  }
  
  private val sendTextEndIconTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
                                  ) {
    }
    
    override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
                              ) {
    }
    
    override fun afterTextChanged(s: Editable) {
      positiveButton.isEnabled = s.isNotBlank()
    }
  }
}
