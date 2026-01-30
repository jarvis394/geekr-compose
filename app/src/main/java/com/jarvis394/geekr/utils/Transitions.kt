package com.jarvis394.geekr.utils

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.jarvis394.geekr.ui.theme.Durations
import com.jarvis394.geekr.ui.theme.Easings
import kotlin.math.roundToInt

fun topLevelTransitionSpec(): ContentTransform =
    ContentTransform(
        fadeIn(
            animationSpec =
                tween(
                    durationMillis = (0.65f * Durations.Short4).roundToInt(),
                    delayMillis = (0.35f * Durations.Short4).roundToInt(),
                    easing = Easings.Emphasized,
                )
        ),
        fadeOut(
            animationSpec =
                tween(
                    durationMillis = (0.35f * Durations.Short4).roundToInt(),
                    easing = Easings.Emphasized,
                )
        ),
    )

fun topLevelPopTransitionSpec(): ContentTransform = topLevelTransitionSpec()

fun sharedAxisXTransitionSpec(): ContentTransform =
    ContentTransform(
        slideInHorizontally(
            animationSpec = tween(durationMillis = Durations.Long1, easing = Easings.Emphasized),
            initialOffsetX = { (0.1f * it).roundToInt() },
        ) +
                fadeIn(
                    animationSpec =
                        tween(
                            durationMillis = (0.65f * Durations.Long1).roundToInt(),
                            delayMillis = (0.35f * Durations.Long1).roundToInt(),
                            easing = Easings.Emphasized,
                        )
                ),
        slideOutHorizontally(
            animationSpec = tween(durationMillis = Durations.Long1, easing = Easings.Emphasized),
            targetOffsetX = { (-0.1f * it).roundToInt() },
        ) +
                fadeOut(
                    animationSpec =
                        tween(
                            durationMillis = (0.35f * Durations.Long1).roundToInt(),
                            easing = Easings.Emphasized,
                        )
                ),
    )

fun sharedAxisXPopTransitionSpec(): ContentTransform =
    ContentTransform(
        slideInHorizontally(
            animationSpec = tween(durationMillis = Durations.Long1, easing = Easings.Emphasized),
            initialOffsetX = { (-0.1f * it).roundToInt() },
        ) +
                fadeIn(
                    animationSpec =
                        tween(
                            durationMillis = (0.65f * Durations.Long1).roundToInt(),
                            delayMillis = (0.35f * Durations.Long1).roundToInt(),
                            easing = Easings.Emphasized,
                        )
                ),
        slideOutHorizontally(
            animationSpec = tween(durationMillis = Durations.Long1, easing = Easings.Emphasized),
            targetOffsetX = { (0.1f * it).roundToInt() },
        ) +
                fadeOut(
                    animationSpec =
                        tween(
                            durationMillis = (0.35f * Durations.Long1).roundToInt(),
                            easing = Easings.Emphasized,
                        )
                ),
    )