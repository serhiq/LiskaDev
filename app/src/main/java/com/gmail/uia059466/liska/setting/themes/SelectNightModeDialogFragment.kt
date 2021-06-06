package com.gmail.uia059466.liska.setting.themes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.Mode
import com.gmail.uia059466.liska.R

class SelectNightModeDialogFragment : AppCompatDialogFragment() {

    var onOk: (() -> Unit)? = null
    lateinit var selectedNow: Mode

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val current = arguments?.getString(current)

        val selected = when {
            current != null -> Mode.fromString(current)
            else -> Mode.SYSTEM
        }

        val modes = Mode.values()
        val titleThemes = modes.map { getTitle(it) }

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.setting_night_mode))
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .setSingleChoiceItems(
              titleThemes.toTypedArray(),
              selected.ordinal
            ) { dialog, position ->
                selectedNow = modes[position]
                onOk?.invoke()
                dialog.dismiss()
            }
            .create()
    }

    private fun getTitle(nightMode: Mode): String {
        return when(nightMode){
            Mode.LIGHT -> getString(R.string.setting_night_mode_disabled)
            Mode.DARK -> getString(R.string.setting_night_mode_enabled)
            Mode.SYSTEM -> getString(R.string.setting_night_mode_system)
        }
    }

    companion object {
        private const val current = "current"

        fun newInstance(mode: Mode): SelectNightModeDialogFragment {
            val dialog = SelectNightModeDialogFragment()
            val args = Bundle().apply {
                putString(current, mode.rawValue)
            }
            dialog.arguments = args
            return dialog
        }
    }
}