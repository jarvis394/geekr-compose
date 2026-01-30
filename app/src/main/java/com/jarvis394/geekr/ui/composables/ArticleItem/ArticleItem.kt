package com.jarvis394.geekr.ui.composables.ArticleItem

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.ui.composables.Image
import java.time.OffsetDateTime

@Composable
fun ArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
    onClick: ((article: Article) -> Unit)? = null
) {
    fun formatRelative(isoString: String?): String {
        if (isoString.isNullOrEmpty()) return ""
        val timeMillis = OffsetDateTime.parse(isoString).toInstant().toEpochMilli()
        return DateUtils.getRelativeTimeSpanString(
            timeMillis, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    val timestamp = formatRelative(article.timePublished)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (onClick != null) {
                    onClick(article)
                }
            },
    ) {
        Column {
            article.leadData?.imageUrl?.let { url ->
                Image(
                    model = url,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(212.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = article.titleHtml,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${article.author.alias} • $timestamp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
            )
        }
    }
}
