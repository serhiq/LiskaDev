package com.gmail.uia059466.liska.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateSingleTop(@IdRes resId: Int, args: Bundle? = null) {
    val hostDestinationId = graph.startDestination
    val navOptions = NavOptions.Builder()
        .setPopUpTo(hostDestinationId, false)
        .setLaunchSingleTop(true)
        .build()
    navigate(resId, args, navOptions)
}
