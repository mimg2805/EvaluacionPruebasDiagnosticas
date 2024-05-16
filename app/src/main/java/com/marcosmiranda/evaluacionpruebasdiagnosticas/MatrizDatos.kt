package com.marcosmiranda.evaluacionpruebasdiagnosticas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.math.BigInteger

class MatrizDatos : Activity() {

    private var a = BigInteger.ZERO
    private var b = BigInteger.ZERO
    private var c = BigInteger.ZERO
    private var d = BigInteger.ZERO
    private var ab = BigInteger.ZERO
    private var cd = BigInteger.ZERO
    private var ac = BigInteger.ZERO
    private var bd = BigInteger.ZERO
    private var total = BigInteger.ZERO

    private lateinit var etA: EditText
    private lateinit var etB: EditText
    private lateinit var etC: EditText
    private lateinit var etD: EditText
    private lateinit var etAB: EditText
    private lateinit var etCD: EditText
    private lateinit var etAC: EditText
    private lateinit var etBD: EditText
    private lateinit var etTotal: EditText
    private lateinit var btnClear: Button
    private lateinit var btnCalc: Button
    private lateinit var tstEmpty: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matriz_datos)

        etA = findViewById(R.id.activity_matriz_datos_et_a)
        etB = findViewById(R.id.activity_matriz_datos_et_b)
        etC = findViewById(R.id.activity_matriz_datos_et_c)
        etD = findViewById(R.id.activity_matriz_datos_et_d)
        etAB = findViewById(R.id.activity_matriz_datos_et_ab)
        etCD = findViewById(R.id.activity_matriz_datos_et_cd)
        etAC = findViewById(R.id.activity_matriz_datos_et_ac)
        etBD = findViewById(R.id.activity_matriz_datos_et_bd)
        etTotal = findViewById(R.id.activity_matriz_datos_et_total)
        btnClear = findViewById(R.id.activity_matriz_datos_btn_clear)
        btnCalc = findViewById(R.id.activity_matriz_datos_btn_calc)
        tstEmpty = Toast.makeText(this, R.string.toast_values_empty, Toast.LENGTH_LONG)

        etA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                a = BigInteger(text.toString())
            }
        })

        etB.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                b = BigInteger(text.toString())
            }
        })

        etC.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                c = BigInteger(text.toString())
            }
        })

        etD.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrBlank()) return
                calc()
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrBlank()) return
                d = BigInteger(text.toString())
            }
        })

        btnClear.setOnClickListener { clear() }

        btnCalc.setOnClickListener {
            etA.clearFocus()
            etB.clearFocus()
            etC.clearFocus()
            etD.clearFocus()

            if (a == BigInteger.ZERO || b == BigInteger.ZERO || c == BigInteger.ZERO || d == BigInteger.ZERO) {
                tstEmpty.cancel()
                tstEmpty.show()
                return@setOnClickListener
            }

            intent = Intent(this, Tema::class.java)
            intent.putExtra("a", a.toString())
            intent.putExtra("b", b.toString())
            intent.putExtra("c", c.toString())
            intent.putExtra("d", d.toString())
            startActivity(intent)
        }
    }

    private fun calc() {
        if (a != BigInteger.ZERO || b != BigInteger.ZERO) ab = a + b
        if (c != BigInteger.ZERO || d != BigInteger.ZERO) cd = c + d
        if (a != BigInteger.ZERO || c != BigInteger.ZERO) ac = a + c
        if (b != BigInteger.ZERO || d != BigInteger.ZERO) bd = b + d
        total = a + b + c + d

        if (ab != BigInteger.ZERO) etAB.setText(String.format(ab.toString()))
        if (cd != BigInteger.ZERO) etCD.setText(String.format(cd.toString()))
        if (ac != BigInteger.ZERO) etAC.setText(String.format(ac.toString()))
        if (bd != BigInteger.ZERO) etBD.setText(String.format(bd.toString()))
        if (total != BigInteger.ZERO) etTotal.setText(String.format(total.toString()))
    }

    private fun clear() {
        a = BigInteger.ZERO
        b = BigInteger.ZERO
        c = BigInteger.ZERO
        d = BigInteger.ZERO
        ab = BigInteger.ZERO
        cd = BigInteger.ZERO
        ac = BigInteger.ZERO
        bd = BigInteger.ZERO
        total = BigInteger.ZERO

        etA.setText("")
        etB.setText("")
        etC.setText("")
        etD.setText("")
        etAB.setText("")
        etCD.setText("")
        etAC.setText("")
        etBD.setText("")
        etTotal.setText("")

        etA.clearFocus()
        etB.clearFocus()
        etC.clearFocus()
        etD.clearFocus()
    }
}