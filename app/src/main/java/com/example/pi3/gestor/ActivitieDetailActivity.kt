package com.example.pi3.gestor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.model.Activitie
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.pi3.data.ActionRepository

class ActivitieDetailActivity : AppCompatActivity() {

    private lateinit var textViewActivitieDetailTitle: TextView
    private lateinit var textViewActivitieDetailDescription: TextView
    private lateinit var textViewActivitieDetailResponsavel: TextView
    private lateinit var textViewActivitieDetailOrcamento: TextView
    private lateinit var textViewActivitieDetailDataInicio: TextView
    private lateinit var textViewActivitieDetailDataFim: TextView
    private lateinit var textViewActivitieDetailStatus: TextView
    private lateinit var buttonChangeStatus: Button
    private lateinit var textViewLinkedActionTitle: TextView

    private lateinit var activitieRepository: ActivitieRepository
    private lateinit var actionRepository: ActionRepository
    private var activitieId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activitie_detail)

        textViewActivitieDetailTitle = findViewById(R.id.textViewActivitieDetailTitle)
        textViewActivitieDetailDescription = findViewById(R.id.textViewActivitieDetailDescription)
        textViewActivitieDetailResponsavel = findViewById(R.id.textViewActivitieDetailResponsavel)
        textViewActivitieDetailOrcamento = findViewById(R.id.textViewActivitieDetailOrcamento)
        textViewActivitieDetailDataInicio = findViewById(R.id.textViewActivitieDetailDataInicio)
        textViewActivitieDetailDataFim = findViewById(R.id.textViewActivitieDetailDataFim)
        textViewActivitieDetailStatus = findViewById(R.id.textViewActivitieDetailStatus)
        buttonChangeStatus = findViewById(R.id.buttonChangeStatus)
        textViewLinkedActionTitle = findViewById(R.id.textViewLinkedActionTitle)

        activitieRepository = ActivitieRepository(this)
        actionRepository = ActionRepository(this)

        activitieId = intent.getLongExtra("activitie_id", -1)

        if (activitieId != -1L) {
            loadActivitieDetails(activitieId)
        } else {
            Toast.makeText(this, "Erro: ID da atividade não fornecido", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonChangeStatus.setOnClickListener {
            changeStatusToConcluida(activitieId)
        }
    }

    private fun loadActivitieDetails(id: Long) {
        val activitie = activitieRepository.getActivityById(id)
        if (activitie != null) {
            textViewActivitieDetailTitle.text = "Título: ${activitie.titulo}"
            textViewActivitieDetailDescription.text = "Descrição: ${activitie.descricao}"
            textViewActivitieDetailResponsavel.text = "Responsável: ${activitie.responsavel}"
            textViewActivitieDetailOrcamento.text = "Orçamento: ${activitie.orcamento}"
            textViewActivitieDetailDataInicio.text = "Data Início: ${activitie.dataInicio}"
            textViewActivitieDetailDataFim.text = "Data Fim: ${activitie.dataFim}"
            textViewActivitieDetailStatus.text = "Status: ${getStatusString(activitie.status)}"

            // Buscar e exibir o título da Ação ligada
            val linkedAction = actionRepository.getActionById(activitie.acaoId)
            textViewLinkedActionTitle.text = linkedAction?.titulo ?: "Ação não encontrada"

            // Habilitar/desabilitar botão de mudança de status
            if (activitie.status == Activitie.STATUS_CONCLUIDA) {
                buttonChangeStatus.visibility = Button.GONE
            } else {
                buttonChangeStatus.visibility = Button.VISIBLE
            }

        } else {
            Toast.makeText(this, "Erro ao carregar detalhes da atividade", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getStatusString(status: Int): String {
        return when (status) {
            Activitie.STATUS_CONCLUIDA -> "Concluída"
            Activitie.STATUS_EM_ANDAMENTO -> "Em andamento"
            Activitie.STATUS_ATRASADA -> "Atrasada"
            else -> "Desconhecido"
        }
    }

    private fun changeStatusToConcluida(id: Long) {
        val success = activitieRepository.updateActivitieStatus(id, Activitie.STATUS_CONCLUIDA)
        if (success) {
            Toast.makeText(this, "Status atualizado para Concluída", Toast.LENGTH_SHORT).show()
            loadActivitieDetails(id) // Recarrega os detalhes para atualizar a tela
        } else {
            Toast.makeText(this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show()
        }
    }
} 