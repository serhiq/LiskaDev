package com.gmail.uia059466.liska.selectfromcatalog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.data.database.CatalogDatabase

class SelectCatalogDialogFragment : AppCompatDialogFragment() {
  private val listDisplay:MutableList<CatalogDatabase> = mutableListOf()
  var onOk: (() -> Unit)? = null
  var selectedNow: Long=0L
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val listTitle= listDisplay.map{it.title}
      val title = requireContext().getString(R.string.dialog_select_catalog_title)
      return AlertDialog.Builder(requireContext())
            .setTitle(title)
            
            .setSingleChoiceItems(
                    listTitle.toTypedArray(),
                    -1
                                 ) { dialog, position ->
              selectedNow = listDisplay[position].id
              onOk?.invoke()
              dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
              // do something
            }
            .create()
  }
  
  companion object {
    fun newInstance(list:List<CatalogDatabase>): SelectCatalogDialogFragment {
      val dialog = SelectCatalogDialogFragment()
      dialog.listDisplay.addAll(list)
      return dialog
    }
  }
}