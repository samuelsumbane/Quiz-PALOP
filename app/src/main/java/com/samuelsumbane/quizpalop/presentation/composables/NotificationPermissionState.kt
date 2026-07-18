package com.samuelsumbane.quizpalop.presentation.composables

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit


private const val PREFS_NAME = "notification_prefs"
private const val KEY_HAS_REQUESTED = "has_requested_notification_permission"

fun hasRequestedBefore(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_HAS_REQUESTED, false)
}

fun markPermissionRequested(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit { putBoolean(KEY_HAS_REQUESTED, true) }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun rememberNotificationPermissionState(): NotificationPermissionState {
    val context = LocalContext.current
    val activity = context as? Activity

    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    var showRationale by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted && activity != null) {
            showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    return remember(hasPermission, showRationale) {
        NotificationPermissionState(
            hasPermission = hasPermission,
            canAskAgain = showRationale,
            requestPermission = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    markPermissionRequested(context) // 👈 marca aqui
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        )
    }
}

data class NotificationPermissionState(
    val hasPermission: Boolean,
    val canAskAgain: Boolean,
    val requestPermission: () -> Unit
)

