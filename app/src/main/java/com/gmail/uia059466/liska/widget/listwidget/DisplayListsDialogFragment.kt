package com.gmail.uia059466.liska.widget.listwidget

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay

class DisplayListsDialogFragment : AppCompatDialogFragment() {
  private val listDisplay:MutableList<ListDisplay> = mutableListOf()
  var onOk: (() -> Unit)? = null
  var selectedNow: Long=0L
  var selectedIndex: Int=0

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val listTitle= listDisplay.map{it.title}
    
    return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.widget_select_list))
            
            .setSingleChoiceItems(
                    listTitle.toTypedArray(),
                    selectedIndex,
                                 ) { dialog, position ->
              selectedNow = listDisplay[position].id
              selectedIndex=position
              onOk?.invoke()
              dialog.dismiss()
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
            }
            .create()
  }
  
  companion object {
    fun newInstance(list:List<ListDisplay>,selected:Int): DisplayListsDialogFragment {
      val dialog = DisplayListsDialogFragment()
      dialog.listDisplay.addAll(list)
      dialog.selectedIndex=selected
      return dialog
    }
  }
}