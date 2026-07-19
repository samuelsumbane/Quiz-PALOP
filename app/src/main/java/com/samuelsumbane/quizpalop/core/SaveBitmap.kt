package com.samuelsumbane.quizpalop.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun saveBitmap(context: Context, bitmap: Bitmap): Uri {

    val file = File(context.cacheDir, "resultado.png")

    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return FileProvider.getUriForFile(
        context, "${context.packageName}.provider", file
    )
}