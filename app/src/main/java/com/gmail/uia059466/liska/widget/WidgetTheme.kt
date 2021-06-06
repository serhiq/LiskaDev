package com.gmail.uia059466.liska.widget

import com.gmail.uia059466.liska.R

enum class WidgetTheme(
    val rawValue: String,
    val background: Int,
    val textColor: Int,
    val iconSetting: Int,
    val iconList: Int,
    val checkboxComplected: Int,
    val checkbox: Int

) {
    DARK(
        rawValue = "dark",
        background = R.drawable.note_widget_background_dark,
        textColor = R.color.d_widget_text_title,
        iconSetting = R.drawable.ic_widget_settings_dark_theme,
        iconList = R.drawable.widget_small_list_dark,
        checkboxComplected = R.drawable.ic_outline_check_box_24px_light,
        checkbox = R.drawable.ic_outline_check_box_outline_blank_24px_light
    ),

    LIGHT(
        rawValue = "light",
        background = R.drawable.note_widget_background_light,
        textColor = R.color.l_widget_text_title,
        iconSetting = R.drawable.ic_widget_settings_light_theme,
        iconList = R.drawable.widget_small_list_light,
        checkboxComplected = R.drawable.ic_outline_check_box_24px,
        checkbox = R.drawable.ic_outline_check_box_outline_blank_24px
    ),
    TRANSPARENT_TEXT_DARK(
        rawValue = "transparent_text_dark",
        background = 0,
        textColor = R.color.l_widget_text_title,
        iconSetting = R.drawable.ic_widget_settings_light_theme,
        iconList = R.drawable.widget_small_list_dark,
        checkboxComplected = R.drawable.ic_outline_check_box_24px,
        checkbox = R.drawable.ic_outline_check_box_outline_blank_24px

    ),
    TRANSPARENT_TEXT_LIGHT(
        rawValue = "transparent_text_light",
        background = 0,
        textColor = R.color.d_widget_text_title,
        iconSetting = R.drawable.widget_ic_setting_dark,
        iconList = R.drawable.widget_small_list_light,
        checkboxComplected = R.drawable.ic_outline_check_box_24px_light,
        checkbox = R.drawable.ic_outline_check_box_outline_blank_24px_light
    );

    companion object {
        fun fromString(raw: String): WidgetTheme {
            return WidgetTheme.values().single { it.rawValue == raw }
        }

        fun toString(value: WidgetTheme): String = value.rawValue

        fun requestNameTheme(theme: WidgetTheme): Int {
            return when (theme) {
                DARK -> R.string.theme_widget_dark
                LIGHT -> R.string.theme_widget_light
                TRANSPARENT_TEXT_DARK -> R.string.theme_widget_transparent_dark_text
                TRANSPARENT_TEXT_LIGHT -> R.string.theme_widget_transparent_light_text
            }
        }
    }
}
