package com.jarvis394.geekr.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jarvis394.geekr.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

import android.content.Intent
import android.app.PendingIntent
import com.jarvis394.geekr.MainActivity

@Singleton
class ArticleNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun showArticleReadyNotification(title: String, articleId: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("article_id", articleId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            articleId.toInt(), // unique request code
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "new_article_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("New Article")
            .setContentText("Your article \"$title\" is ready!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, we cannot show the notification
                return
            }
            notify("new_article_${System.currentTimeMillis()}".hashCode(), builder.build())
        }
    }
}
