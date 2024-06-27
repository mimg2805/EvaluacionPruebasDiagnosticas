package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class MainMenu : Activity() {

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Ads
        startAds()

        val tvAppName = findViewById<TextView>(R.id.activity_main_menu_tv_app_name)
        val appNameStr = getString(R.string.app_name)
        val appNameChrArr = appNameStr.toCharArray()
        var strCurrentPos = 0
        val strLen = appNameStr.length

        val timer = Timer()
        val taskEverySplitSecond: TimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    tvAppName.setText(appNameChrArr, 0, strCurrentPos)
                }
                strCurrentPos++

                if (strCurrentPos == strLen) {
                    timer.cancel()
                }
            }
        }
        timer.schedule(taskEverySplitSecond, 1, 100)

        val btnEvaluar = findViewById<Button>(R.id.activity_main_menu_btn_evaluate)
        btnEvaluar.setOnClickListener {
            timer.cancel()
            tvAppName.text = appNameStr
            startActivity(Intent(this, MatrizDatos::class.java))
        }

        val btnMasApps = findViewById<Button>(R.id.activity_main_menu_btn_more_apps)
        btnMasApps.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Marcos+I.+Miranda+G.")))
        }
    }

    public override fun onPause() {
        adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adView.resume()
    }

    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private fun startAds()
    {
        // Initialize the Google Mobile Ads SDK on a background thread.
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainMenu)
        }

        // Create a new ad view.
        adView = AdView(this)
        adView.setAdSize(adSize)
        adView.adUnitId = getString(R.string.banner_ad_id)

        adViewContainer = findViewById(R.id.activity_main_menu_fl_ads_container)
        adViewContainer.addView(adView)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    private val adSize: AdSize
    get() {
        // Determine the screen width (less decorations) to use for the ad width.
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        // If the ad hasn't been laid out, default to the full screen width.
        var adWidthPixels = adView.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }
}