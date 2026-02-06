package com.jarvis394.geekr.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.ui.MainAppScreenKey
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
fun ProfileScreen(navigator: Navigator<MainAppScreenKey>) {
    fun navigateUp() {
        navigator.navigateBack()
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Profile Screen")
            }
        }
    }
}