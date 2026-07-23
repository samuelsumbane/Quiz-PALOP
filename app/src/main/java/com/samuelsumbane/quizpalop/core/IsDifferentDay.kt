package com.samuelsumbane.quizpalop.core

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun isDifferentDay(
    timestamp1: Long,
    timestamp2: Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): Boolean {
    val date1 = Instant.ofEpochMilli(timestamp1)
        .atZone(zoneId)
        .toLocalDate()

    val date2 = Instant.ofEpochMilli(timestamp2)
        .atZone(zoneId)
        .toLocalDate()

    return date1 != date2
}