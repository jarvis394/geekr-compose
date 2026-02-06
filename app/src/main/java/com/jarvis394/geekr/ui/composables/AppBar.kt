package com.jarvis394.geekr.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jarvis394.geekr.ui.articles.ArticlesFilter
import com.jarvis394.geekr.ui.profile.UserProfile
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials


@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    hazeState: HazeState,
    currentFilter: ArticlesFilter,
    onAvatarClick: () -> Unit,
    profile: UserProfile? = null
) {
    val bgColor = MaterialTheme.colorScheme.background
    val appBarShape = RoundedCornerShape(24.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to bgColor, 1f to Color.Transparent
                    )
                )
            }
            .statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent, CircleShape)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                            appBarShape
                        )
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.regular(),
                        )
                        .size(40.dp)
                        .innerShadow(
                            CircleShape,
                            Shadow(8.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f))
                        ), onClick = { println("Menu was clicked") }) {
                    Icon(
                        imageVector = Icons.Sharp.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(appBarShape)
                    .clickable(
                        onClick = { println("AppBar was clicked") })
                    .background(Color.Transparent, appBarShape)
                    .border(
                        1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f), appBarShape
                    )
                    .hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular(),
                    )
                    .innerShadow(
                        appBarShape,
                        Shadow(8.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f))
                    )
                    .padding(start = 16.dp, end = 12.dp)
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        4.dp, Alignment.CenterHorizontally
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentFilter.extendedLabel,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "Chevron Down",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Image(
                model = profile?.avatarUrl,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onAvatarClick)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .border(
                        1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f), appBarShape
                    )
            )
        }
    }
}