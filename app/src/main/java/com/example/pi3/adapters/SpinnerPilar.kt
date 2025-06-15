package com.example.pi3.adapters

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

//Componente do Pilar
fun setupPilarSpinner(
    context: Context,
    spinner: Spinner,
    onItemSelected: (String) -> Unit
) {
    val pilares = arrayOf(
        "1. Suporte da alta administração", "2. Instância responsável",
        "3. Análise de riscos", "4.1. Código de ética e conduta e políticas de compliance",
        "4.2. Comunicação e treinamento", "4.3. Ouvidoria", "4.4. Investigações internas",
        "4.5. Processos de investigação", "4.6. Due diligence", "5. Diversidade e inclusão",
        "6. Auditoria e monitoramento"
    )

    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, pilares)
    spinner.adapter = adapter

    spinner.setSelection(0)

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            onItemSelected(pilares[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Você pode tratar algo aqui, se quiser
        }
    }
}