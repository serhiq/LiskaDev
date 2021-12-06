package com.gmail.uia059466.liska.setting.selectcatalog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R

class CatalogDisplayOptionDialog : AppCompatDialogFragment() {

    var selected: CatalogDisplayOption? = null

    var onOk: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fromArg = requireNotNull(arguments?.getString(CURRENT_OPTION)?.let { CatalogDisplayOption.fromString(it) })
        val options = CatalogDisplayOption.values()

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_catalog_select_option_title))
            .setNegativeButton(android.R.string.cancel) { _, _ -> 0}
            .setSingleChoiceItems(
                options.map { getString(it.title)}.toTypedArray(),
                fromArg.ordinal
            ) { dialog, position ->
                selected = options[position]
                onOk?.invoke()
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        private const val CURRENT_OPTION = "current_option"

        fun newInstance(currentTheme: CatalogDisplayOption): CatalogDisplayOptionDialog {
            return CatalogDisplayOptionDialog().apply {
                arguments = Bundle().apply {
                    putString(CURRENT_OPTION, currentTheme.rawValue)
                }
            }
        }
    }
}