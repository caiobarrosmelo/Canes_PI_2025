package com.example.pi3.adapters

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun setupPilarSpinner(
    context: Context,
    spinner: Spinner,
    onItemSelected: (String) -> Unit
) {
    val pilares = arrayOf(
        "Suporte da Alta Administração", "Instância Responsável",
        "Análise de Riscos", "Estruturação das Regras e Instrumentos",
        "Código de Ética/Compliance", "Ouvidoria", "Investigações Internas",
        "Processo de Investigação", "Due Diligence", "Diversidade e Inclusão",
        "Auditoria e Monitoramento"
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