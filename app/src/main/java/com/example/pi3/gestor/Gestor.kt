package com.example.pi3.gestor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pi3.R
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Activitie
import com.example.pi3.model.Action
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.*

class Gestor : AppCompatActivity() {

    private lateinit var spinnerPilar: Spinner
    private lateinit var pieChartAcoes: PieChart
    private lateinit var actionRepository: ActionRepository
    private lateinit var btnExportReport: Button
    private lateinit var btnHistory: Button
    private lateinit var btnLogout: TextView
    private lateinit var reportGenerator: ReportGenerator

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestor)

        // Inicialização dos componentes
        spinnerPilar = findViewById(R.id.spinnerPilar)
        pieChartAcoes = findViewById(R.id.pieChartAcoes)
        btnExportReport = findViewById(R.id.btnExportReport)
        btnHistory = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogout)
        actionRepository = ActionRepository(this)
        reportGenerator = ReportGenerator(this)

        // Configuração do Spinner
        val pilares = arrayOf("Todos", "1. Suporte da alta administração", "2. Instância responsável",
            "3. Análise de riscos", "4.1. Código de ética e conduta e políticas de compliance",
            "4.2. Comunicação e treinamento", "4.3. Ouvidoria", "4.4. Investigações internas",
            "4.5. Processos de investigação", "4.6. Due diligence", "5. Diversidade e inclusão",
            "6. Auditoria e monitoramento")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pilares)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPilar.adapter = adapter

        // Gráficos
        setupPieCharts()

        spinnerPilar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateCharts()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        updateCharts()

        // Botão Histórico
        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }

        // Botão Exportar Relatório
        btnExportReport.setOnClickListener {
            showExportConfirmationDialog()
        }

        // Botão Logout
        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, com.example.pi3.MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showExportConfirmationDialog() {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        AlertDialog.Builder(this)
            .setTitle("Exportar Relatório")
            .setMessage("Deseja exportar o relatório do pilar '$pilarSelecionado'?")
            .setPositiveButton("Sim") { _, _ ->
                if (checkPermission()) {
                    exportReport()
                } else {
                    requestPermission()
                }
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportReport()
            } else {
                Toast.makeText(
                    this,
                    "Permissão necessária para exportar o relatório",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun exportReport() {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        val pdfUri = reportGenerator.generateReport(pilarSelecionado)

        if (pdfUri != null) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Compartilhar relatório"))
        } else {
            Toast.makeText(
                this,
                "Erro ao gerar o relatório",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupPieCharts() {
        pieChartAcoes.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleRadius(0f)

            legend.apply {
                isEnabled = true
                textColor = Color.BLACK
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

        val acoes = if (pilarSelecionado == "Todos") {
            actionRepository.getAllActions()
        } else {
            actionRepository.getActionsByPilar(pilarSelecionado)
        }.filter { it.aprovada }

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

        val temEmAndamento = atividades.any { it.status == Activitie.STATUS_EM_ANDAMENTO && dateFormat.parse(it.dataFim)?.after(hoje) == true }
        val temAtrasada = atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }

        return atividades.isNotEmpty() && temEmAndamento && !temAtrasada && !isActionConcluida(action)
    }

    private fun isActionAtrasada(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }
    }
}
