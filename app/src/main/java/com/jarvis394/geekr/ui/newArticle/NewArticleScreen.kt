package com.jarvis394.geekr.ui.newArticle

import android.util.Log
import com.jarvis394.geekr.ui.composables.TextEditor.TextControls
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.composables.Navigator
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.serialization.Serializable
import com.jarvis394.geekr.ui.personalArticles.PersonalArticleScreenKey

@Serializable
data class NewArticleScreenKey(val articleId: Long? = null) : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.newArticleScreenEntry(
    navigator: Navigator<MainAppScreenKey>
) {
    entry<NewArticleScreenKey> { key -> NewArticleScreen(navigator, key.articleId) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewArticleScreen(
    navigator: Navigator<MainAppScreenKey>,
    articleId: Long? = null,
    viewModel: NewArticleViewModel = hiltViewModel()
) {
    val isPreviewMode by remember { mutableStateOf(false) }
    val titleState = rememberTextFieldState()
    val textState = rememberRichTextState()

    val context = androidx.compose.ui.platform.LocalContext.current
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(articleId) {
        if (articleId != null) {
            viewModel.loadPersonalArticle(articleId) { title, content ->
                titleState.edit {
                    replace(0, length, title)
                }
                Log.d("NewArticleScreen", "content: $content")
                textState.setHtml(
                    content.replace(Regex("""<img\b[^>]*>(?:.*?</img>)?""", RegexOption.IGNORE_CASE)) {
                        val src = Regex("""src="([^"]*)"""").find(it.value)?.groupValues?.get(1) ?: ""
                        val alt = Regex("""alt="([^"]*)"""").find(it.value)?.groupValues?.get(1) ?: ""
                        "![$alt]($src)"
                    }
                )
            }
        }
    }

    val scrollState = rememberScrollState()

    fun navigateUp() {
        navigator.navigateBack()
    }

    fun saveArticle() {
        val html = textState.toHtml().replace(
            Regex("""&excl;&lsqb;(.+?)&rsqb;&lpar;(.+?)&rpar;""", RegexOption.IGNORE_CASE),
            "<img src=\"$2\" alt=\"$1\">"
        )
        Log.d("NewArticleScreen", "html: ${textState.toHtml()}")
        Log.d("NewArticleScreen", "parsed html: $html")
        viewModel.savePersonalArticle(
            titleState.text.toString(), html, articleId
        ) { id ->
            if (articleId != null && navigator.backStack.size > 1) {
                navigator.navigateBack()
            }
            navigator.replace(PersonalArticleScreenKey(id, showSavedSnackbar = true))
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                if (articleId != null) "Edit article" else "New draft",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"
                )
            }
        }, actions = {
            TextButton(onClick = { saveArticle() }) {
                Text("Save")
            }
        })
    }, bottomBar = {
        Surface(tonalElevation = 2.dp) {
            TextControls(state = textState)
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(scrollState),
        ) {
            TextField(
                state = titleState, colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ), textStyle = MaterialTheme.typography.headlineMedium, placeholder = {
                    Text(
                        text = "Title", style = MaterialTheme.typography.headlineMedium
                    )
                })

            if (isPreviewMode) {
                RichText(
                    state = textState, modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            } else {
                RichTextEditor(
                    state = textState,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("Add some text here...") },
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}