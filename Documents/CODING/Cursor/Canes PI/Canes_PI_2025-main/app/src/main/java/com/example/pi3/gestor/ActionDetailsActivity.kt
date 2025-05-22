package com.example.pi3.gestor

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pi3.R
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActionDetailsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var textViewDetailTitle: TextView
    private lateinit var textViewDetailDescription: TextView
    private lateinit var textViewDetailResponsible: TextView
    private lateinit var textViewDetailDates: TextView
    private lateinit var textViewDetailPilar: TextView
    private lateinit var textViewDetailStatus: TextView
    private lateinit var progressBarDetail: ProgressBar
    private lateinit var statusIndicatorDetail: View

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_details)

        btnBack = findViewById(R.id.btnBack)
        textViewDetailTitle = findViewById(R.id.textViewDetailTitle)
        textViewDetailDescription = findViewById(R.id.textViewDetailDescription)
        textViewDetailResponsible = findViewById(R.id.textViewDetailResponsible)
        textViewDetailDates = findViewById(R.id.textViewDetailDates)
        textViewDetailPilar = findViewById(R.id.textViewDetailPilar)
        textViewDetailStatus = findViewById(R.id.textViewDetailStatus)
        progressBarDetail = findViewById(R.id.progressBarDetail)
        statusIndicatorDetail = findViewById(R.id.statusIndicatorDetail)

        val action = intent.getParcelableExtra<Action>("action")

        if (action != null) {
            textViewDetailTitle.text = action.titulo
            textViewDetailDescription.text = action.descricao
            textViewDetailResponsible.text = "Responsável: ${action.responsavel}"
            textViewDetailDates.text = "Período: ${action.dataInicio} - ${action.dataFim}"
            textViewDetailPilar.text = "Pilar: ${action.pilar}"

            val dataAtual = Date()
            val dataFim = try { dateFormat.parse(action.dataFim) } catch (e: Exception) { null }

            val statusText = when {
                action.aprovada && dataFim != null && !dataFim.after(dataAtual) -> {
                    statusIndicatorDetail.background = ContextCompat.getDrawable(this, R.drawable.status_indicator_green)
                    "Concluída"
                }
                dataFim != null && dataFim.after(dataAtual) -> {
                    statusIndicatorDetail.background = ContextCompat.getDrawable(this, R.drawable.status_indicator_yellow)
                    "Em Andamento"
                }
                else -> {
                    statusIndicatorDetail.background = ContextCompat.getDrawable(this, R.drawable.status_indicator_red)
                    "Atrasada"
                }
            }
            textViewDetailStatus.text = "Status: $statusText"

            // Implementar a lógica do progresso aqui se necessário
            // progressBarDetail.progress = calcularProgresso(action.id)

        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    // Função placeholder para calcular progresso (se aplicável)
    // private fun calcularProgresso(actionId: Long): Int {
    //     // Lógica para calcular progresso com base nas atividades associadas à ação
    //     return 0
    // }
}