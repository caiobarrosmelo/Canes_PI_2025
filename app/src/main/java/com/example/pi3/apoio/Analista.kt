package com.example.pi3.apoio

import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R

class Analista : AppCompatActivity() {

    private lateinit var spinnerPilar: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analista)

        spinnerPilar = findViewById(R.id.spinnerPilar)
    }
}