package com.gmail.uia059466.liska.listdetail

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay

class SendSelectedDialogFragment : AppCompatDialogFragment() {
  private val listDisplay:MutableList<ListDisplay> = mutableListOf()
  var onOk: (() -> Unit)? = null
  var selectedNow: Long=0L
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val listTitle= listDisplay.map{it.title}
    
    return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sendselecteddialogtitle))
            
            .setSingleChoiceItems(
                    listTitle.toTypedArray(),
                    -1
                                 ) { dialog, position ->
              selectedNow = listDisplay[position].id
              onOk?.invoke()
              dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .create()
  }
  
  companion object {
    fun newInstance(list:List<ListDisplay>): SendSelectedDialogFragment {
      val dialog = SendSelectedDialogFragment()
      dialog.listDisplay.addAll(list)
      return dialog
    }
  }
}