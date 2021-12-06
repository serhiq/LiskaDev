package com.gmail.uia059466.liska

import androidx.annotation.StringRes

enum class Mode(val rawValue: String, @StringRes val title: Int) {

    DARK("dark", R.string.setting_night_mode_enabled),
    LIGHT("light", R.string.setting_night_mode_disabled),
    SYSTEM("system", R.string.setting_night_mode_system);

    companion object {
        fun fromString(raw: String) = values().single { it.rawValue == raw }

        fun toString(value: Mode): String = value.rawValue
    }
}