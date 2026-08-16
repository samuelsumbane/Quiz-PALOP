package org.quizpalop.app.core

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import org.quizpalop.app.R


class SoundManager(context: Context) {

    private val soundPool: SoundPool

    private var correctId = 0
    private var wrongId = 0
    private var clickId = 0
    private var coinsEarned = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        correctId = soundPool.load(context, R.raw.correct_chime, 1)
        wrongId = soundPool.load(context, R.raw.incorrect_chime, 1)
        clickId = soundPool.load(context, R.raw.soft_tap, 1)
        coinsEarned = soundPool.load(context, R.raw.coin_earned, 1)
    }

    fun playCorrect() {
        soundPool.play(correctId, 0.8f, 0.8f, 1, 0, 1f)
    }

    fun playWrong() {
        soundPool.play(wrongId, 0.8f, 0.8f, 1, 0, 1f)
    }

    fun playClick() {
        soundPool.play(clickId, 0.8f, 0.8f, 1, 0, 1f)
    }

    fun playCoinsEarned() {
        soundPool.play(coinsEarned, 0.7f, 0.7f, 1, 0, 1f)
    }


    fun release() {
        soundPool.release()
    }
}