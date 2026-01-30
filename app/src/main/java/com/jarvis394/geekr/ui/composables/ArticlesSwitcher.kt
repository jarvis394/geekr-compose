package com.jarvis394.geekr.ui.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jarvis394.geekr.ui.articles.ArticlesFilter

val LeadingButtonShape = RoundedCornerShape(
    topStart = 16.dp, topEnd = 8.dp, bottomStart = 16.dp, bottomEnd = 8.dp
)
val PressedLeadingButtonShape = RoundedCornerShape(
    topStart = 12.dp, topEnd = 4.dp, bottomStart = 12.dp, bottomEnd = 4.dp
)
val ButtonShape = RoundedCornerShape(8.dp)
val PressedButtonShape = RoundedCornerShape(4.dp)
val CheckedButtonShape = RoundedCornerShape(16.dp)
val TrailingButtonShape = RoundedCornerShape(
    topStart = 8.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 16.dp
)
val PressedTrailingButtonShape = RoundedCornerShape(
    topStart = 4.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 12.dp
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArticlesSwitcherRow(
    selectedFilter: ArticlesFilter, onFilterSelected: (ArticlesFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        ArticlesFilter.entries.forEachIndexed { index, filter ->
            ToggleButton(
                checked = selectedFilter == filter,
                onCheckedChange = { onFilterSelected(filter) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes(
                        shape = LeadingButtonShape,
                        checkedShape = CheckedButtonShape,
                        pressedShape = PressedLeadingButtonShape
                    )

                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes(
                        shape = ButtonShape,
                        checkedShape = CheckedButtonShape,
                        pressedShape = PressedButtonShape
                    )
                },
            ) {
                Text(filter.label)
            }
        }
        ToggleButton(
            checked = false,
            // TODO: implement
            onCheckedChange = { },
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp),
            contentPadding = PaddingValues(0.dp),
            shapes = ButtonGroupDefaults.connectedTrailingButtonShapes(
                shape = TrailingButtonShape,
                checkedShape = CheckedButtonShape,
                pressedShape = PressedTrailingButtonShape
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = "Filter",
            )
        }
    }
}