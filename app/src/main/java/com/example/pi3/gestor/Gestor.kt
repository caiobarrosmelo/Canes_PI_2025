package com.example.pi3.gestor

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.R
import com.example.pi3.data.ActionRepository
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.pi3.model.Activitie
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.Button
import android.content.Intent
import java.util.Calendar
import com.github.mikephil.charting.components.Legend

class Gestor : AppCompatActivity() {
    private lateinit var spinnerPilar: Spinner
    private lateinit var pieChartAcoes: PieChart
    private lateinit var actionRepository: ActionRepository
    private lateinit var btnExportReport: Button
    private lateinit var btnHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestor)

        // Inicialização dos componentes
        spinnerPilar = findViewById(R.id.spinnerPilar)
        pieChartAcoes = findViewById(R.id.pieChartAcoes)
        actionRepository = ActionRepository(this)
        btnExportReport = findViewById(R.id.btnExportReport)
        btnHistory = findViewById(R.id.btnHistory)

        // Configuração do Spinner de Pilares
        val pilares = arrayOf("Todos", "1. Suporte da alta administração", "2. Instância responsável",
            "3. Análise de riscos", "4.1. Código de ética e conduta e políticas de compliance",
            "4.2. Comunicação e treinamento", "4.3. Ouvidoria", "4.4. Investigações internas",
            "4.5. Processos de investigação", "4.6. Due diligence", "5. Diversidade e inclusão",
            "6. Auditoria e monitoramento")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pilares)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPilar.adapter = adapter

        // Configuração dos gráficos
        setupPieCharts()

        // Atualizar gráficos quando o pilar for alterado
        spinnerPilar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateCharts()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não é necessário fazer nada aqui
            }
        }

        // Carregar dados iniciais
        updateCharts()

        // Configuração do botão Histórico
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupPieCharts() {
        // Configuração do gráfico de ações
        pieChartAcoes.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleRadius(0f)

            // Configuração da legenda
            legend.apply {
                isEnabled = true
                textColor = Color.BLACK // Usar texto preto para melhor contraste no fundo branco
                textSize = 12f
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
                xEntrySpace = 10f
                yEntrySpace = 5f
                setWordWrapEnabled(true)
            }

            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            setDrawEntryLabels(false)
            setUsePercentValues(true)
        }
    }

    private fun updateCharts() {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        
        // Obter dados do repositório
        val acoes = if (pilarSelecionado == "Todos") {
            actionRepository.getAllActions()
        } else {
            actionRepository.getActionsByPilar(pilarSelecionado)
        }.filter { it.aprovada }

        // Contar status das ações
        val acoesConcluidas = acoes.count { isActionConcluida(it) }
        val acoesEmAndamento = acoes.count { isActionEmAndamento(it) }
        val acoesAtrasadas = acoes.count { isActionAtrasada(it) }

        val entriesAcoes = listOf(
            PieEntry(acoesConcluidas.toFloat(), "Concluídas"),
            PieEntry(acoesEmAndamento.toFloat(), "Em andamento"),
            PieEntry(acoesAtrasadas.toFloat(), "Atrasadas")
        )

        val dataSetAcoes = PieDataSet(entriesAcoes, "")
        val colors = listOf(
            Color.parseColor("#006400"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#8B0000")
        )
        dataSetAcoes.colors = colors
        pieChartAcoes.data = PieData(dataSetAcoes)
        pieChartAcoes.invalidate()
    }

    private fun isActionConcluida(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        return atividades.isNotEmpty() && atividades.all { it.status == Activitie.STATUS_CONCLUIDA }
    }

    private fun isActionEmAndamento(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val temAtividadeEmAndamento = atividades.any { it.status == Activitie.STATUS_EM_ANDAMENTO && dateFormat.parse(it.dataFim)?.after(hoje) == true }
        val temAtividadeAtrasada = atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }

        return atividades.isNotEmpty() && temAtividadeEmAndamento && !temAtividadeAtrasada && !isActionConcluida(action)
    }

    private fun isActionAtrasada(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }
    }
}