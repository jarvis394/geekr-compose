package com.jarvis394.geekr.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jarvis394.geekr.utils.ScrimDirection
import com.jarvis394.geekr.utils.fadingScrim

@Composable
fun InsetBottom() {
    val bgColor = MaterialTheme.colorScheme.background.copy(0.9f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .fadingScrim(bgColor, ScrimDirection.BottomToTop)
    )
}