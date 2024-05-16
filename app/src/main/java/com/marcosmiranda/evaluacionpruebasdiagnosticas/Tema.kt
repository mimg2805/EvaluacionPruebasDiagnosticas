package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import org.json.JSONArray
import java.io.File

class Tema : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tema)

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
            val temaNombre = tema.getString("nombre")
            val temaHtml = tema.getString("html")

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
}