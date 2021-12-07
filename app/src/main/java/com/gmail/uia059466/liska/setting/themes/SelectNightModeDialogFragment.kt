package com.gmail.uia059466.liska.setting.themes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.gmail.uia059466.liska.data.Mode
import com.gmail.uia059466.liska.R

class SelectNightModeDialogFragment : AppCompatDialogFragment() {

    var onOk: (() -> Unit)? = null
    var selected: Mode? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val current = requireNotNull(arguments?.getString(ARG_CURRENT_MODE)?.let { Mode.fromString(it) })
        val modes = Mode.values()

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.setting_night_mode))
            .setNegativeButton(android.R.string.cancel) { _, _ ->
            }
            .setSingleChoiceItems(
                modes.map { getString(it.title)}.toTypedArray(),
                current.ordinal
            ) { dialog, position ->
                this.selected = modes[position]
                onOk?.invoke()
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        private const val ARG_CURRENT_MODE = "current"

        fun newInstance(mode: Mode): SelectNightModeDialogFragment {
            return SelectNightModeDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CURRENT_MODE, mode.rawValue)
                }
            }
        }
    }
}