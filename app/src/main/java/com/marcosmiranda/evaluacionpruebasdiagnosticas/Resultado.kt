package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale

class Resultado : Activity() {

    private val mc = MathContext(4, RoundingMode.HALF_UP)

    private var a = BigDecimal.ZERO
    private var b = BigDecimal.ZERO
    private var c = BigDecimal.ZERO
    private var d = BigDecimal.ZERO

    private var result = BigDecimal.ZERO
    private var resultStr = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val wv = findViewById<WebView>(R.id.activity_resultado_wv)
        val tvResult = findViewById<TextView>(R.id.activity_resultado_tv_result)

        val htmlFile = intent.getStringExtra("temaHtml")
        wv.settings.javaScriptEnabled = true
        wv.settings.domStorageEnabled = true
        wv.loadUrl("file:///android_asset/html/$htmlFile")

        a = BigDecimal(intent.getStringExtra("a").toString())
        b = BigDecimal(intent.getStringExtra("b").toString())
        c = BigDecimal(intent.getStringExtra("c").toString())
        d = BigDecimal(intent.getStringExtra("d").toString())

        val ab = a + b
        val ac = a + c
        val ad = a + d
        val bd = b + d
        val cd = c + d
        val total = a + b + c + d

        val temaId = intent.getIntExtra("temaId", 0)
        val temaNombre = intent.getStringExtra("temaNombre")
        result = when (temaId) {
            1       -> ad.divide(total, mc) * BigDecimal(100) // Exactitud
            2       -> a.divide(ac, mc) * BigDecimal(100) // Sensibilidad
            3       -> d.divide(bd, mc) * BigDecimal(100) // Especificidad
            4       -> a.divide(ab, mc) * BigDecimal(100) // VPP
            5       -> d.divide(cd, mc) * BigDecimal(100) // VPN
            6       -> (a.divide(ac, mc)).divide(b.divide(bd, mc), mc) // RVP
            7       -> (c.divide(ac, mc)).divide(d.divide(bd, mc), mc) // RVN
            8       -> b.divide(bd, mc) * BigDecimal(100) // RFP
            9       -> c.divide(ac, mc) * BigDecimal(100) // RFN
            10      -> b.divide(ab, mc) * BigDecimal(100) // RFD
            11      -> c.divide(cd, mc) * BigDecimal(100) // RFO
            12      -> ((a.divide(ab, mc) + d.divide(cd, mc)) - BigDecimal.ONE) * BigDecimal(100) // IM
            13      -> ((a.divide(ac, mc) + d.divide(bd, mc)) - BigDecimal.ONE) * BigDecimal(100) // II
            else    -> BigDecimal.ZERO
        }

        resultStr = "$temaNombre = " + when (temaId) {
            6, 7    -> String.format(Locale.ENGLISH, "%.2f", result) // RVP / RVN
            else    -> String.format(Locale.ENGLISH, "%.2f", result) + "%" // El resto
        }
        tvResult.text = resultStr
    }
}