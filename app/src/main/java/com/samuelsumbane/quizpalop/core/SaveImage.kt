package com.samuelsumbane.quizpalop.core

import android.content.Context
import android.content.Intent
import android.net.Uri

fun shareImage(
    context: Context,
    uri: Uri
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(intent, "Partilhar resultado")
    )
}