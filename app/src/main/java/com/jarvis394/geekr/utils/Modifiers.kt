package com.jarvis394.geekr.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class ScrimDirection { TopToBottom, BottomToTop }

fun Modifier.fadingScrim(
    bgColor: Color,
    direction: ScrimDirection = ScrimDirection.TopToBottom
): Modifier = this.drawBehind {
    val baseAlpha = bgColor.alpha

    val gradientStops = arrayOf(
        0.000f to 1.000f,
        0.190f to 0.738f,
        0.340f to 0.541f,
        0.470f to 0.382f,
        0.565f to 0.278f,
        0.650f to 0.194f,
        0.730f to 0.126f,
        0.802f to 0.075f,
        0.861f to 0.042f,
        0.910f to 0.021f,
        0.952f to 0.008f,
        0.982f to 0.002f,
        1.000f to 0.000f
    )

    val finalStops = gradientStops.map { (pos, stopAlpha) ->
        val effectivePos = if (direction == ScrimDirection.BottomToTop) 1f - pos else pos
        effectivePos to bgColor.copy(alpha = baseAlpha * stopAlpha)
    }.let { it -> if (direction == ScrimDirection.BottomToTop) it.sortedBy { it.first } else it }

    drawRect(
        brush = Brush.verticalGradient(
            colorStops = finalStops.toTypedArray()
        )
    )
}
