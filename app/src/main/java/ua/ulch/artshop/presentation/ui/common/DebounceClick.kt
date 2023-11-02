package ua.ulch.artshop.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

inline fun Modifier.debounceClickable(
    debounceInterval: Long = 400,
    crossinline onClick: () -> Unit,
): Modifier {
    var lastClickTime = 0L
    return clickable() {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}