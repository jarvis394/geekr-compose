package com.jarvis394.geekr.ui.composables

import android.text.Html
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val annotatedString = remember(html) {
        val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        buildAnnotatedString {
            append(spanned.toString())
        }
    }

    Text(text = annotatedString, modifier = modifier)
}