package com.example.pi3.gestor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3.MainActivity
import com.example.pi3.R
import com.example.pi3.data.ActionRepository
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.example.pi3.gestor.HistoricoActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Gestor : AppCompatActivity() {
    private lateinit var spinnerPilar: Spinner
    private lateinit var pieChartAcoes: PieChart
    private lateinit var btnSair: ImageButton
    private lateinit var btnExportar: Button
    private lateinit var btnHistorico: Button
    private lateinit var actionRepository: ActionRepository

    // Cores padronizadas
    private val COR_CONCLUIDO = Color.parseColor("#2E7D32") // Verde escuro
    private val COR_EM_ANDAMENTO = Color.parseColor("#F9A825") // Amarelo escuro
    private val COR_ATRASADO = Color.parseColor("#C62828") // Vermelho escuro

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestor)

        initializeViews()
        setupSpinner()
        setupPieChart()
        setupButtons()
        updateChart()
    }

    private fun initializeViews() {
        spinnerPilar = findViewById(R.id.spinnerPilar)
        pieChartAcoes = findViewById(R.id.pieChartAcoes)
        btnSair = findViewById(R.id.btnSair)
        btnExportar = findViewById(R.id.btnExportar)
        btnHistorico = findViewById(R.id.btnHistorico)
        actionRepository = ActionRepository(this)
    }

    private fun setupSpinner() {
        val pilares = arrayOf("Todos", "1. Suporte da alta administração", "2. Instância responsável",
            "3. Análise de riscos", "4.1. Código de ética e conduta e políticas de compliance",
            "4.2. Comunicação e treinamento", "4.3. Ouvidoria", "4.4. Investigações internas",
            "4.5. Processos de investigação", "4.6. Due diligence", "5. Diversidade e inclusão",
            "6. Auditoria e monitoramento")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pilares)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPilar.adapter = adapter

        spinnerPilar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateChart()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não é necessário fazer nada aqui
            }
        }
    }

    private fun setupPieChart() {
        pieChartAcoes.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleRadius(0f)
            legend.isEnabled = false // Usamos legenda customizada
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            setDrawEntryLabels(false)
            setUsePercentValues(true)
        }
    }

    private fun setupButtons() {
        btnSair.setOnClickListener {
            // Voltar para a tela de login principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExportar.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento - Exportar Relatório", Toast.LENGTH_SHORT).show()
        }

        btnHistorico.setOnClickListener {
            // Iniciar a HistoricoActivity
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateChart() {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        val acoes = if (pilarSelecionado == "Todos") {
            actionRepository.getAllActions()
        } else {
            actionRepository.getActionsByPilar(pilarSelecionado)
        }

        if (acoes.isEmpty()) {
            showEmptyChart()
            return
        }

        val dataAtual = Date()
        val acoesConcluidas = acoes.count { action ->
            action.aprovada && dateFormat.parse(action.dataFim)?.before(dataAtual) == true
        }
        val acoesEmAndamento = acoes.count { action ->
            (action.aprovada && dateFormat.parse(action.dataFim)?.after(dataAtual) == true) ||
            (!action.aprovada && dateFormat.parse(action.dataFim)?.after(dataAtual) == true)
        }
        val acoesAtrasadas = acoes.count { action ->
            !action.aprovada && dateFormat.parse(action.dataFim)?.before(dataAtual) == true
        }

        val entries = mutableListOf<PieEntry>()
        val cores = mutableListOf<Int>()
        if (acoesConcluidas > 0) {
            entries.add(PieEntry(acoesConcluidas.toFloat(), "Concluído"))
            cores.add(COR_CONCLUIDO)
        }
        if (acoesEmAndamento > 0) {
            entries.add(PieEntry(acoesEmAndamento.toFloat(), "Em Andamento"))
            cores.add(COR_EM_ANDAMENTO)
        }
        if (acoesAtrasadas > 0) {
            entries.add(PieEntry(acoesAtrasadas.toFloat(), "Atrasado"))
            cores.add(COR_ATRASADO)
        }

        val dataSet = PieDataSet(entries, "Status das Ações")
        dataSet.colors = cores
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        pieChartAcoes.data = PieData(dataSet)
        pieChartAcoes.invalidate()
    }

    private fun showEmptyChart() {
        val entries = listOf(PieEntry(1f, "Sem dados"))
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.GRAY)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        pieChartAcoes.data = PieData(dataSet)
        pieChartAcoes.invalidate()
    }
}