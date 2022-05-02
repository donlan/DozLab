package com.example.base.extensions

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun Fragment.showOtherFragment(fragment: Fragment, @IdRes containerId: Int) {
    childFragmentManager.beginTransaction()
        .add(containerId, fragment)
        .show(fragment)
        .hide(this)
        .addToBackStack(fragment::class.java.simpleName)
        .commit()
}

fun Fragment.navController(@IdRes id: Int): NavController {
    val navHostFragment =
        requireActivity().supportFragmentManager.findFragmentById(id) as NavHostFragment
    return navHostFragment.navController
}

fun Fragment.getColorCompat(@ColorRes id: Int) = requireContext().getColorCompat(id)

