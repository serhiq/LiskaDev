package com.gmail.uia059466.liska.setting.selectcatalog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R

class SelectCatalogDialogFragment : AppCompatDialogFragment() {

    var onOk: (() -> Unit)? = null
    lateinit var selectedNow:SelectCatalogOption

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val current = arguments?.getString(CURRENT_OPTION)

        val selected = when {
            current != null -> SelectCatalogOption.fromString(current)
            else -> SelectCatalogOption.TWO_VAR
        }

        val themes = SelectCatalogOption.values()
        val titleThemes = themes.map { getTitleForOption(it) }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_catalog_select_option_title))
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .setSingleChoiceItems(
              titleThemes.toTypedArray(),
              selected.ordinal
            ) { dialog, position ->
                selectedNow = themes[position]
                onOk?.invoke()
                dialog.dismiss()
            }
            .create()
    }

    private fun getTitleForOption(theme: SelectCatalogOption) = when (theme) {
        SelectCatalogOption.CHECKBOX -> getString(R.string.dialog_select)
        SelectCatalogOption.SELECT-> getString(R.string.dialog_quantity)
        SelectCatalogOption.TWO_VAR-> getString(R.string.dialog_two)
    }

    companion object {
        private const val CURRENT_OPTION = "current_option"

        fun newInstance(currentTheme: SelectCatalogOption): SelectCatalogDialogFragment {
            val dialog = SelectCatalogDialogFragment()
            val args = Bundle().apply {
                putString(CURRENT_OPTION, currentTheme.rawValue)
            }
            dialog.arguments = args
            return dialog
        }
    }
}