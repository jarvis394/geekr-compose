package com.jarvis394.geekr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jarvis394.geekr.R

@OptIn(ExperimentalTextApi::class)
val displayRegularFontFamily = FontFamily(
    Font(
        R.font.googlesansflex_variable, variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val displayMediumFontFamily = FontFamily(
    Font(
        R.font.googlesansflex_variable, variationSettings = FontVariation.Settings(
            FontVariation.weight(500),
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val displayBoldFontFamily = FontFamily(
    Font(
        R.font.googlesansflex_variable, variationSettings = FontVariation.Settings(
            FontVariation.weight(700),
        )
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = displayMediumFontFamily,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = displayMediumFontFamily,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

