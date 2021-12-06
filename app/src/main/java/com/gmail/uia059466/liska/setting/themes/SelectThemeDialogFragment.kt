package com.gmail.uia059466.liska.setting.themes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R

class SelectThemeDialogFragment : AppCompatDialogFragment() {

    var selected: Theme? = null

    var onOk: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val current = requireNotNull(arguments?.getString(ARG_CURRENT_THEME)?.let { Theme.fromString(it) })
        val themes = Theme.values()

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_select_themes_title))
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .setSingleChoiceItems(
                themes.map { getString(it.title) }.toTypedArray(),
                current.ordinal
            ) { dialog, position ->
                selected = themes[position]
                onOk?.invoke()
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        private const val ARG_CURRENT_THEME = "current_theme"

        fun newInstance(currentTheme: Theme): SelectThemeDialogFragment {
            return SelectThemeDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CURRENT_THEME, currentTheme.rawValue)
                }
            }
        }
    }
}