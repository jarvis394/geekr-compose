package com.jarvis394.geekr.ui.composables

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView

class ContentUriWebViewClient(private val context: Context) : WebViewClient() {
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val url = request?.url ?: return null

        if (url.scheme == "content" || url.scheme == "file") {
            return try {
                val inputStream = if (url.scheme == "content") {
                    context.contentResolver.openInputStream(url)
                } else {
                    java.io.FileInputStream(java.io.File(url.path ?: ""))
                }
                
                val mimeType = if (url.scheme == "content") {
                    context.contentResolver.getType(url) ?: "image/jpeg"
                } else {
                    val extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(url.toString())
                    android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "image/jpeg"
                }

                WebResourceResponse(mimeType, "UTF-8", inputStream)
            } catch (e: Exception) {
                android.util.Log.e("HtmlText", "Error intercepting request", e)
                null
            }
        }
        return super.shouldInterceptRequest(view, request)
    }
}

fun Color.toHtmlHex(): String = String.format("#%06X", (0xFFFFFF and this.toArgb()))

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val backgroundHex = colors.background.toHtmlHex()
    val textHex = colors.onBackground.toHtmlHex()
    val primaryHex = colors.primary.toHtmlHex()
    val codeBgHex = colors.surfaceVariant.toHtmlHex()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                setBackgroundColor(0)
                webViewClient = ContentUriWebViewClient(context)
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    allowFileAccess = true
                    allowFileAccessFromFileURLs = true
                    allowUniversalAccessFromFileURLs = true
                }
            }
        },
        update = { webView ->
            val styledHtml = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <script src="https://cdn.tailwindcss.com?plugins=typography"></script>
                    <script>
                        tailwind.config = {
                            theme: {
                                extend: {
                                typography: {
                                    colors: {
                                        brand: '$primaryHex',
                                        bg: '$backgroundHex',
                                        fg: '$textHex',
                                    }
                                }
                            }
                        }
                    </script>
                    <style type="text/tailwindcss">
                        @utility prose-system {
                          --tw-prose-body: $textHex;
                          --tw-prose-headings: $textHex;
                          --tw-prose-lead: $textHex;
                          --tw-prose-links: $textHex;
                          --tw-prose-bold: $textHex;
                          --tw-prose-counters: $textHex;
                          --tw-prose-bullets: $textHex;
                          --tw-prose-hr: $codeBgHex;
                          --tw-prose-quotes: $textHex;
                          --tw-prose-quote-borders: $codeBgHex;
                          --tw-prose-captions: $textHex;
                          --tw-prose-code: $textHex;
                          --tw-prose-pre-code: $codeBgHex;
                          --tw-prose-pre-bg: $textHex;
                          --tw-prose-th-borders:$codeBgHex;
                          --tw-prose-td-borders: $codeBgHex;
                          --tw-prose-invert-body: $codeBgHex;
                          --tw-prose-invert-headings: $backgroundHex;
                        }
                    
                        @layer base {
                          body { background-color: $backgroundHex !important; color: $textHex !important; }
                          .prose { color: $textHex !important; }
                          .prose * { color: $textHex !important; }
                          img { @apply rounded-md; width: auto !important; height: auto !important; max-width: 100% !important; margin: 0 auto; display: block; }
                        }
                    </style>
                </head>
                <body class="p-4">
                    <article class="prose prose-system max-w-none">
                        $html
                    </article>
                </body>
                </html>
            """.trimIndent()

            webView.loadDataWithBaseURL("file:///android_asset/", styledHtml, "text/html", "utf-8", null)
        }
    )
}