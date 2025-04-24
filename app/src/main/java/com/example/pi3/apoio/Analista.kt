package com.example.pi3.apoio

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R

class Analista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analista)

        val spinner: Spinner = findViewById(R.id.spinnerPilar)

        val pilarList = listOf("Pilar 01", "Pilar 02", "Pilar 03", "Pilar 04", "Pilar 05", "Pilar 06")

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_selected_item,  // item selecionado
            pilarList
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) // itens do menu suspenso

        spinner.adapter = adapter
    }
}