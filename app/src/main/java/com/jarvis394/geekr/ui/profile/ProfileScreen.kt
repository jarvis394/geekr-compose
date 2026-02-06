package com.jarvis394.geekr.ui.profile

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.composables.Image
import com.jarvis394.geekr.ui.composables.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object ProfileScreenKey : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.profileScreenEntry(
    navigator: Navigator<MainAppScreenKey>
) {
    entry<ProfileScreenKey> { key -> ProfileScreen(navigator) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigator: Navigator<MainAppScreenKey>, viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val profile by viewModel.userProfile.collectAsStateWithLifecycle(null)
    var aliasInput by remember { mutableStateOf("") }
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { avatar ->
        if (avatar != null) {
            context.contentResolver.takePersistableUriPermission(
                avatar, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.updateProfile(
                aliasInput, avatar.toString()
            )
            Toast.makeText(context, "Avatar uploaded", Toast.LENGTH_SHORT).show()
        }
    }

    fun navigateUp() {
        navigator.navigateBack()
    }

    LaunchedEffect(profile) {
        profile?.let { aliasInput = it.alias }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Profile")
        }, navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"
                )
            }
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.size(56.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }, contentAlignment = Alignment.Center
                    ) {
                        if (profile?.avatarUrl.isNullOrEmpty()) {
                            Text("Upload picture", style = MaterialTheme.typography.labelSmall)
                        } else {
                            Image(
                                model = profile?.avatarUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    IconButton(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent, CircleShape),
                        onClick = {
                            viewModel.updateProfile(aliasInput, avatarUrl = null)
                            Toast.makeText(context, "Avatar deleted", Toast.LENGTH_SHORT).show()
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete avatar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = aliasInput,
                    onValueChange = {
                        aliasInput = it
                    },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(aliasInput, profile?.avatarUrl ?: "")
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save")
                }
            }
        }
    }
}