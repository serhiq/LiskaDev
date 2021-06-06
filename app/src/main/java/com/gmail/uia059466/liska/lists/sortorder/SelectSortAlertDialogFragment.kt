package com.gmail.uia059466.liska.lists.sortorder

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R

class SelectSortAlertDialogFragment : AppCompatDialogFragment() {
  
  var onOk: (() -> Unit)? = null
  var selected = SortOrder.A_Z
  
  
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val sortTitleInt = arguments?.getInt(EXTRA_TITLE_SORT_INT) ?: R.string.title_sort_default
    val sortTitle = getString(sortTitleInt)
    
    val strSortOption = arguments?.getString(EXTRA_SORT_OPTION) ?: ""
    val selectedSortOrder = SortOrder.fromString(strSortOption)
    
    val sorted = SortOrder.values().map { it.rawValue }
    val allTitle = SortOrder.values().map { getTitleForSortOption(it) }
    
    
    val checkedItem = sorted.indexOf(selectedSortOrder.rawValue)
    
    return AlertDialog.Builder(requireContext())
            .setTitle(sortTitle)
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            
            .setSingleChoiceItems(
                    allTitle.toTypedArray(),
                    checkedItem
                                 ) { dialogInterface, i ->
              val mumu = sorted[i]
              selected = SortOrder.fromString(mumu)
              onOk?.invoke()
              dialogInterface.dismiss()
            }.create()
    
  }
  
  private fun getTitleForSortOption(sortOption: SortOrder) = when (sortOption) {
    SortOrder.LAST_MODIFIED -> getString(R.string.last_modificated)
    SortOrder.A_Z -> getString(R.string.sort_alphabetical)
    SortOrder.NEWEST_FIRST -> getString(R.string.sort_date_added)
    SortOrder.MANUAL_SORT -> getString(R.string.sort_by_human)
  }
  
  
  companion object {
    
    private const val EXTRA_SORT_OPTION = "sort_option"
    private const val EXTRA_TITLE_SORT_INT = "sort_title"
    
    fun newInstance(textSortOption: SortOrder, titleInt: Int): SelectSortAlertDialogFragment {
      val dialog = SelectSortAlertDialogFragment()
      val args = Bundle().apply {
        putString(EXTRA_SORT_OPTION, textSortOption.rawValue)
        putInt(EXTRA_TITLE_SORT_INT, titleInt)
      }
      dialog.arguments = args
      return dialog
    }
  }
}

