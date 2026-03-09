package com.jarvis394.geekr.ui.composables.TextEditor

import android.net.Uri
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.jarvis394.geekr.utils.GeekrFileProvider
import com.mohamedrejeb.richeditor.model.RichTextState

fun insertHtmlImage(state: RichTextState, url: String) {
    state.addTextAfterSelection("\n![image]($url)\n")
}

fun copyUriToCache(context: Context, uri: Uri): String? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = java.io.File(context.cacheDir, fileName)
        val outputStream = java.io.FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return "file://" + file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddImageIconButton(state: RichTextState) {
    val context = LocalContext.current
    var showAddMenu by remember { mutableStateOf(false) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            uri?.let {
                val localUri = copyUriToCache(context, it)
                if (localUri != null) {
                    insertHtmlImage(state, localUri)
                }
                // onImageSelected(it)
            }
        })

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(), onResult = { success ->
            if (success) {
                tempPhotoUri?.let {
                    val localUri = copyUriToCache(context, it)
                    if (localUri != null) {
                        insertHtmlImage(state, localUri)
                    }
                    // onImageSelected(it)
                }
            }
        })

    fun launchCamera() {
        val uri = GeekrFileProvider.getImageUri(context)
        tempPhotoUri = uri
        cameraLauncher.launch(uri)
    }

    ControlIcon(
        icon = Icons.Default.AddPhotoAlternate,
        isSelected = false,
        onClick = { showAddMenu = true })

    if (showAddMenu) {
        ModalBottomSheet(onDismissRequest = { showAddMenu = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ListItem(
                    headlineContent = { Text("Take photo") },
                    leadingContent = { Icon(Icons.Default.PhotoCamera, null) },
                    modifier = Modifier.clickable {
                        showAddMenu = false
                        launchCamera()
                    })
                ListItem(
                    headlineContent = { Text("Choose image") },
                    leadingContent = { Icon(Icons.Default.Image, null) },
                    modifier = Modifier.clickable {
                        showAddMenu = false
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    })
            }
        }
    }
}

@Composable
fun TextControls(
    state: RichTextState, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp)
            .navigationBarsPadding()
            .imePadding(), verticalAlignment = Alignment.CenterVertically
    ) {
        AddImageIconButton(state)
        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .padding(horizontal = 8.dp)
        )
        ControlIcon(
            icon = Icons.Default.FormatBold,
            isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
            onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) })
        ControlIcon(
            icon = Icons.Default.FormatItalic,
            isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
            onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) })
        ControlIcon(
            icon = Icons.Default.FormatUnderlined,
            isSelected = state.currentSpanStyle.textDecoration == TextDecoration.Underline,
            onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) })

        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .padding(horizontal = 8.dp)
        )
        ControlIcon(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            isSelected = state.isUnorderedList,
            onClick = { state.toggleUnorderedList() })
    }
}

@Composable
fun ControlIcon(
    icon: ImageVector, isSelected: Boolean, onClick: () -> Unit
) {
    IconButton(
        onClick = onClick, colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}