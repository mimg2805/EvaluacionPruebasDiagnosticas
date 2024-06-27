package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class Tema : Activity() {

    private lateinit var adViewContainer : FrameLayout
    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tema)

        // Ads
        startAds()

        // Language
        val lang = resources.configuration.locales.get(0).language

        val llTemas = findViewById<LinearLayout>(R.id.activity_tema_ll_temas)
        val llLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llLayoutParams.gravity = Gravity.CENTER
        llLayoutParams.setMargins(0, 0, 0, 64)

        val inputStream = assets.open("json/temas.json")
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()
        val jsonStr = String(byteArray, charset("UTF-8"))
        Log.e("json", jsonStr)

        val temas = JSONArray(jsonStr)
        Log.e("temas", temas.toString())
        val temasLen = temas.length() - 1
        for (t in 0..temasLen) {
            val tema = temas.getJSONObject(t)
            val temaId = tema.getInt("id")
            val temaNombreEs = tema.getString("nombre_es")
            val temaNombreEn = tema.getString("nombre_en")
            val temaHtmlEs = tema.getString("html_es")
            val temaHtmlEn = tema.getString("html_en")

            val temaNombre = if (lang == "en") temaNombreEn
            else temaNombreEs

            val temaHtml = if (lang == "en") "html_en/$temaHtmlEn"
            else "html_es/$temaHtmlEs"

            val btn = Button(this)
            btn.setBackgroundColor(Color.WHITE)
            btn.setTextColor(Color.BLACK)
            btn.textSize = 14f
            btn.text = temaNombre
            btn.textAlignment = View.TEXT_ALIGNMENT_CENTER
            btn.layoutParams = llLayoutParams

            btn.setOnClickListener {
                val intent = Intent(this, Resultado::class.java)
                intent.putExtra("temaId", temaId)
                intent.putExtra("temaNombre", temaNombre)
                intent.putExtra("temaHtml", temaHtml)
                intent.putExtra("a", this.intent.getStringExtra("a").toString())
                intent.putExtra("b", this.intent.getStringExtra("b").toString())
                intent.putExtra("c", this.intent.getStringExtra("c").toString())
                intent.putExtra("d", this.intent.getStringExtra("d").toString())
                startActivity(intent)
            }

            llTemas.addView(btn)
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
            MobileAds.initialize(this@Tema)
        }

        // Create a new ad view.
        adView = AdView(this)
        adView.setAdSize(adSize)
        adView.adUnitId = getString(R.string.banner_ad_id)

        adViewContainer = findViewById(R.id.activity_tema_fl_ads_container)
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