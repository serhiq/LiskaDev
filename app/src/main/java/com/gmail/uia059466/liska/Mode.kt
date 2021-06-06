package com.gmail.uia059466.liska

enum class Mode(val rawValue:String) {

    DARK("dark"),LIGHT("light"),SYSTEM("system");
    companion object {
        fun fromString(raw: String): Mode {
            return Mode.values().single { it.rawValue == raw }
        }
        fun toString(value: Mode): String = value.rawValue
    }
}
