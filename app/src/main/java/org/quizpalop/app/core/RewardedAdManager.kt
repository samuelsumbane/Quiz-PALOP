package org.quizpalop.app.core

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(
    private val context: Context,
) {

    private var rewardedAd: RewardedAd? = null

    fun loadAd(
        onLoaded: (() -> Unit)? = null,
        onFailed: (LoadAdError) -> Unit
    ) {
        /**
         * In development always use adUnitId for test
         * it is: "ca-app-pub-3940256099942544/5224354917"
         */
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),

            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onLoaded?.invoke()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    onFailed(error)
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
            loadAd(onFailed = {})
        }
    }
}