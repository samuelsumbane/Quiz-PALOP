package com.samuelsumbane.quizpalop.domain.repository

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null

    fun loadAd(
        onLoaded: (() -> Unit)? = null
    ) {
        RewardedAd.load(
            context,
            "ca-app-pub",
            AdRequest.Builder().build(),

            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onLoaded?.invoke()
                }

                override fun onAdFailedToLoad(
                    error: LoadAdError
                ) {
                    rewardedAd = null
                    println("ads: Code: ${error.code} Message: ${error.message} Domain: ${error.domain} Response: ${error.responseInfo}")
                }
            }
        )
    }

    fun isReady(): Boolean {
        return rewardedAd != null
    }

    fun show(
        activity: Activity,
        onReward: () -> Unit
    ) {

        rewardedAd?.show(activity) {
            onReward()

            rewardedAd = null
            loadAd()
        }
    }
}