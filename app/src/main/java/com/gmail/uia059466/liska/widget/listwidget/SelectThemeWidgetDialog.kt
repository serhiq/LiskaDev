package com.gmail.uia059466.liska.lists.sortorder

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.widget.WidgetTheme

class SelectThemeWidgetDialogFragment : AppCompatDialogFragment() {

    var onOk: (() -> Unit)? = null
    var selected = WidgetTheme.LIGHT

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title= getString(R.string.theme_widget_title)

        val strThemeSelect = arguments?.getString(EXTRA_THEME) ?: ""
        val theme = WidgetTheme.fromString(strThemeSelect)

        val themes = WidgetTheme.values().map { it.rawValue }
        val allTitle = WidgetTheme.values().map { getTitle(it) }

        val indexChecked = themes.indexOf(theme.rawValue)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }

            .setSingleChoiceItems(
                allTitle.toTypedArray(),
                indexChecked
            ) { dialogInterface, i ->
                val mumu = themes[i]
                selected = WidgetTheme.fromString(mumu)
                onOk?.invoke()
                dialogInterface.dismiss()
            }.create()

    }


    private fun getTitle(theme: WidgetTheme) = when (theme) {
        WidgetTheme.DARK -> getString(R.string.theme_widget_dark)
        WidgetTheme.LIGHT -> getString(R.string.theme_widget_light)
        WidgetTheme.TRANSPARENT_TEXT_DARK -> getString(R.string.theme_widget_transparent_dark_text)
        WidgetTheme.TRANSPARENT_TEXT_LIGHT ->getString(R.string.theme_widget_transparent_light_text)
    }


    companion object {

        private const val EXTRA_THEME = "theme"

        fun newInstance(currentTheme: WidgetTheme): SelectThemeWidgetDialogFragment {
            val dialog = SelectThemeWidgetDialogFragment()
            val args = Bundle().apply {
                putString(EXTRA_THEME, currentTheme.rawValue)
            }
            dialog.arguments = args
            return dialog
        }
    }
}

