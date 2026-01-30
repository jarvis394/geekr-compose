package com.jarvis394.geekr.ui.composables

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.jarvis394.geekr.compat.removeLastCompat

abstract class Navigator<T : NavKey>(val backStack: NavBackStack<T>) {
    val currentKey: T
        get() = backStack.last()

    abstract fun navigateTo(key: T)

    fun navigateBack() {
        backStack.removeLastCompat()
    }
}

class ScreenNavigator<T : NavKey>(backStack: NavBackStack<T>) : Navigator<T>(backStack) {
    override fun navigateTo(key: T) {
        backStack.add(key)
    }
}

class PaneNavigator<T : NavKey>(backStack: NavBackStack<T>) : Navigator<T>(backStack) {
    override fun navigateTo(key: T) {
        if (backStack.last() != key) {
            while (backStack.size > 1) {
                backStack.removeLastCompat()
            }
            if (backStack.last() != key) {
                backStack.add(key)
            }
        }
    }
}