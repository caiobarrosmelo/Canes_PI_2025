package com.example.pi3.adapters

import com.example.pi3.R
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

    // usa nosso layout customizado para o texto colorido
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_item,
        pilares
    )
    adapter.setDropDownViewResource(R.layout.spinner_item)

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
            // sem seleção
        }
    }
}