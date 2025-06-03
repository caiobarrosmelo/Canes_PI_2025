package com.example.pi3.gestor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R
import com.example.pi3.data.ActionRepository

class ActionDetailActivity : AppCompatActivity() {

    private lateinit var textViewActionDetailTitle: TextView
    private lateinit var textViewActionDetailDescription: TextView
    private lateinit var textViewActionDetailResponsavel: TextView
    private lateinit var textViewActionDetailOrcamento: TextView
    private lateinit var textViewActionDetailDataInicio: TextView
    private lateinit var textViewActionDetailDataFim: TextView
    private lateinit var textViewActionDetailPilar: TextView

    private lateinit var actionRepository: ActionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_detail)

        // Inicializar componentes do layout
        textViewActionDetailTitle = findViewById(R.id.textViewActionDetailTitle)
        textViewActionDetailDescription = findViewById(R.id.textViewActionDetailDescription)
        textViewActionDetailResponsavel = findViewById(R.id.textViewActionDetailResponsavel)
        textViewActionDetailOrcamento = findViewById(R.id.textViewActionDetailOrcamento)
        textViewActionDetailDataInicio = findViewById(R.id.textViewActionDetailDataInicio)
        textViewActionDetailDataFim = findViewById(R.id.textViewActionDetailDataFim)
        textViewActionDetailPilar = findViewById(R.id.textViewActionDetailPilar)

        actionRepository = ActionRepository(this)

        // Obter o ID da Ação da Intent
        val actionId = intent.getLongExtra("action_id", -1)

        if (actionId != -1L) {
            // Buscar os detalhes da Ação no repositório
            val action = actionRepository.getActionById(actionId)

            // Preencher os campos com os detalhes da Ação
            if (action != null) {
                textViewActionDetailTitle.text = action.titulo
                textViewActionDetailDescription.text = action.descricao
                textViewActionDetailResponsavel.text = action.responsavel
                textViewActionDetailOrcamento.text = action.orcamento.toString()
                textViewActionDetailDataInicio.text = action.dataInicio
                textViewActionDetailDataFim.text = action.dataFim
                textViewActionDetailPilar.text = action.pilar
            }
        } else {
            // Tratar caso o ID da Ação não seja válido (opcional)
            textViewActionDetailTitle.text = "Erro ao carregar detalhes da Ação"
        }
    }
} 