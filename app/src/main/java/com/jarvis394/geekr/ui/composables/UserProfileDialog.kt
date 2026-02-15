package com.jarvis394.geekr.ui.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.profile.ProfileScreenKey
import com.jarvis394.geekr.ui.profile.ProfileViewModel
import com.jarvis394.geekr.ui.profile.UserProfile

val dialogShape = RoundedCornerShape(28.dp)

data class ListItem(
    val label: String, val icon: ImageVector? = null, val onClick: () -> Unit
)

@Composable
fun ExpressiveList(
    modifier: Modifier = Modifier, items: List<ListItem>
) {
    val cornerRadius = 20.dp

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items.forEachIndexed { index, item ->
            val shape = when {
                items.size == 1 -> RoundedCornerShape(cornerRadius)
                index == 0 -> RoundedCornerShape(
                    topStart = cornerRadius,
                    topEnd = cornerRadius,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                )

                index == items.size - 1 -> RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = cornerRadius,
                    bottomEnd = cornerRadius
                )

                else -> RoundedCornerShape(4.dp)
            }

            Surface(
                onClick = item.onClick,
                shape = shape,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = {
                        Text(item.label, style = MaterialTheme.typography.labelLarge)
                    },
                    leadingContent = item.icon?.let {
                        {
                            Icon(
                                it,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun UserProfileDialog(
    navigator: Navigator<MainAppScreenKey>,
    profile: UserProfile?,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onClose: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val items = listOf(
        ListItem(
            label = "Profile",
            icon = Icons.Rounded.Person,
            onClick = { navigator.navigateTo(ProfileScreenKey) }),
        ListItem(
            label = "Log out", icon = Icons.AutoMirrored.Rounded.Logout, onClick = {
                viewModel.deleteProfile()
                onClose()
            }),
    )

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Surface(
                modifier = Modifier
                    .padding(24.dp)
                    .sharedBounds(
                        rememberSharedContentState(key = "avatar_bounds"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 1f,
                        renderInOverlayDuringTransition = true,
                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        clipInOverlayDuringTransition = OverlayClip(dialogShape),
                    )
                    .zIndex(1f)
                    .background(
                        MaterialTheme.colorScheme.surface, dialogShape
                    )
                    .clickable(enabled = false) {}
                    .fillMaxWidth(),
                shape = dialogShape,
                tonalElevation = 1.dp,
                shadowElevation = 6.dp) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateEnterExit(
                            enter = fadeIn(tween(400, 100)), exit = fadeOut(tween(200))
                        ), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        model = profile?.avatarUrl,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        profile?.alias ?: "Default User",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ExpressiveList(items = items)
                }
            }
        }
    }
}