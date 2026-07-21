package com.samuelsumbane.quizpalop.presentation.dailychallenge

import android.content.Context
import androidx.compose.ui.graphics.layer.GraphicsLayer

sealed interface DailyChallengeUiEvents {
    data class OnCheckResponse(val questionOption: String) : DailyChallengeUiEvents
    data class OnPrintScree(val context: Context, val graphicsLayer: GraphicsLayer) : DailyChallengeUiEvents
}