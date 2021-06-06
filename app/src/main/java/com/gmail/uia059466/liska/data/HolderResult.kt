package com.gmail.uia059466.liska.data

sealed class HolderResult<out R> {

    data class Success<out T>(val data: T) : HolderResult<T>()
    data class Error(val exception: Exception) : HolderResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
