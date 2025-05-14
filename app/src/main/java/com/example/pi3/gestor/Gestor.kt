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

class Gestor : AppCompatActivity() {
    private lateinit var spinnerPilar: Spinner
    private lateinit var pieChartAcoes: PieChart
    private lateinit var pieChartAtividades: PieChart
    private lateinit var actionRepository: ActionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestor)

        // Inicialização dos componentes
        spinnerPilar = findViewById(R.id.spinnerPilar)
        pieChartAcoes = findViewById(R.id.pieChartAcoes)
        pieChartAtividades = findViewById(R.id.pieChartAtividades)
        actionRepository = ActionRepository(this)

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
    }

    private fun setupPieCharts() {
        // Configuração do gráfico de ações
        pieChartAcoes.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleRadius(0f)
            legend.isEnabled = true
            legend.textColor = Color.WHITE
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
        }

        // Configuração do gráfico de atividades
        pieChartAtividades.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleRadius(0f)
            legend.isEnabled = true
            legend.textColor = Color.WHITE
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
        }
    }

    private fun updateCharts() {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        
        // Obter dados do repositório
        val acoes = if (pilarSelecionado == "Todos") {
            actionRepository.getAllActions()
        } else {
            actionRepository.getActionsByPilar(pilarSelecionado)
        }

        // Preparar dados para o gráfico de ações
        val acoesAprovadas = acoes.count { action -> action.aprovada }
        val acoesPendentes = acoes.size - acoesAprovadas

        val entriesAcoes = listOf(
            PieEntry(acoesAprovadas.toFloat(), "Aprovadas"),
            PieEntry(acoesPendentes.toFloat(), "Pendentes")
        )

        val dataSetAcoes = PieDataSet(entriesAcoes, "Status das Ações")
        dataSetAcoes.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieChartAcoes.data = PieData(dataSetAcoes)
        pieChartAcoes.invalidate()

        // Preparar dados para o gráfico de atividades
        val atividades = acoes.flatMap { action -> actionRepository.getActivitiesByActionId(action.id) }
        val atividadesConcluidas = atividades.count { atividade -> atividade.status }
        val atividadesPendentes = atividades.size - atividadesConcluidas

        val entriesAtividades = listOf(
            PieEntry(atividadesConcluidas.toFloat(), "Concluídas"),
            PieEntry(atividadesPendentes.toFloat(), "Pendentes")
        )

        val dataSetAtividades = PieDataSet(entriesAtividades, "Status das Atividades")
        dataSetAtividades.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieChartAtividades.data = PieData(dataSetAtividades)
        pieChartAtividades.invalidate()
    }
}