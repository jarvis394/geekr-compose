package com.jarvis394.geekr.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object GeekrFileProvider {
    fun getImageUri(context: Context): Uri {
        val directory = File(context.cacheDir, "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File.createTempFile(
            "captured_image_${System.currentTimeMillis()}",
            ".jpg",
            directory
        )

        val authority = "${context.packageName}.fileprovider"

        return FileProvider.getUriForFile(
            context,
            authority,
            file
        )
    }
}