package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.util.Timer
import java.util.TimerTask


class MainMenu : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

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

        val btnEvaluar = findViewById<Button>(R.id.activity_main_menu_btn_evaluar)
        btnEvaluar.setOnClickListener {
            timer.cancel()
            tvAppName.text = appNameStr
            startActivity(Intent(this, MatrizDatos::class.java))
        }
    }
}