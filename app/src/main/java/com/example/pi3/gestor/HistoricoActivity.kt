package com.example.pi3.gestor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.data.ActionRepository
import com.example.pi3.data.StatusAtividade
import com.example.pi3.model.Activitie
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast

//Classe que renderiza o histórico de atividades no fluxo do gestor
class HistoricoActivity : AppCompatActivity(), HistoricoAdapter.OnItemClickListener {

    private lateinit var spinnerFilterType: Spinner
    private lateinit var spinnerFilterStatus: Spinner
    private lateinit var spinnerFilterResponsavel: Spinner
    private lateinit var recyclerViewHistorico: RecyclerView
    private lateinit var activitieRepository: ActivitieRepository
    private lateinit var actionRepository: ActionRepository
    private lateinit var historicoAdapter: HistoricoAdapter
    private var allItems: List<Any> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        spinnerFilterType = findViewById(R.id.spinnerFilterType)
        spinnerFilterStatus = findViewById(R.id.spinnerFilterStatus)
        spinnerFilterResponsavel = findViewById(R.id.spinnerFilterResponsavel)
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistorico)
        activitieRepository = ActivitieRepository(this)
        actionRepository = ActionRepository(this)

        setupSpinners()
        setupRecyclerView()
        loadAllItems()
    }

    private fun setupSpinners() {
        // Spinner de Tipo (Geral, Ações, Atividades)
        val filterTypeOptions = arrayOf("Histórico Geral", "Histórico de Ações", "Histórico de Atividades")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterTypeOptions)
        spinnerFilterType.adapter = typeAdapter

        // Spinner de Status (Tudo, Concluída, Pendente, Atrasada)
        val filterStatusOptions = arrayOf("Tudo", "Concluída", "Em andamento", "Atrasada")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterStatusOptions)
        spinnerFilterStatus.adapter = statusAdapter

        // Spinner de Responsável (Todos, Apoio, Coordenador)
        val filterResponsavelOptions = arrayOf("Todos", "Apoio", "Coordenador")
        val responsavelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterResponsavelOptions)
        spinnerFilterResponsavel.adapter = responsavelAdapter

        spinnerFilterType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterItems()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não faz nada
            }
        }

        spinnerFilterStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterItems()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não faz nada
            }
        }

        spinnerFilterResponsavel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadAllItems()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não faz nada
            }
        }
    }

    private fun setupRecyclerView() {
        historicoAdapter = HistoricoAdapter(listOf(), this)
        recyclerViewHistorico.layoutManager = LinearLayoutManager(this)
        recyclerViewHistorico.adapter = historicoAdapter
    }

    private fun loadAllItems() {
        val selectedResponsavel = spinnerFilterResponsavel.selectedItem?.toString() ?: "Todos"
        val allActions = when (selectedResponsavel) {
            "Todos" -> actionRepository.getAllActions()
            "Apoio" -> actionRepository.getAllActions().filter { it.responsavel.equals("apoio", ignoreCase = true) }
            "Coordenador" -> actionRepository.getAllActions().filter { it.responsavel.equals("coordenador", ignoreCase = true) }
            else -> actionRepository.getAllActions()
        }
        val allActivities = when (selectedResponsavel) {
            "Todos" -> activitieRepository.getAllApprovedActivities()
            "Apoio" -> activitieRepository.getApprovedActivitiesByResponsavel("apoio")
            "Coordenador" -> activitieRepository.getApprovedActivitiesByResponsavel("coordenador")
            else -> activitieRepository.getAllApprovedActivities()
        }
        allItems = allActions + allActivities
        filterItems()
    }

    private fun filterItems() {
        val selectedType = spinnerFilterType.selectedItem.toString()
        val selectedStatus = spinnerFilterStatus.selectedItem.toString()
        val selectedResponsavel = spinnerFilterResponsavel.selectedItem.toString()

        val filteredList = allItems.filter { item ->
            val typeMatch = when (selectedType) {
                "Histórico Geral" -> true
                "Histórico de Ações" -> item is Action
                "Histórico de Atividades" -> item is Activitie
                else -> false
            }

            val statusMatch = when (selectedStatus) {
                "Tudo" -> true
                "Concluída" -> if (item is Activitie) item.status == StatusAtividade.CONCLUIDA else false
                "Em andamento" -> if (item is Activitie) item.status == StatusAtividade.EM_PROCESSO else true
                "Atrasada" -> if (item is Activitie) isItemAtrasada(item) else false
                else -> false
            }

            val responsavelMatch = when (selectedResponsavel) {
                "Todos" -> true
                "Apoio" -> {
                    when (item) {
                        is Activitie -> item.responsavel.equals("apoio", ignoreCase = true)
                        is Action -> item.responsavel.equals("apoio", ignoreCase = true)
                        else -> false
                    }
                }
                "Coordenador" -> {
                    when (item) {
                        is Activitie -> item.responsavel.equals("coordenador", ignoreCase = true)
                        is Action -> item.responsavel.equals("coordenador", ignoreCase = true)
                        else -> false
                    }
                }
                else -> false
            }

            typeMatch && statusMatch && responsavelMatch
        }

        // Ordenar a lista combinada por data (mais recente primeiro)
        val sortedList = filteredList.sortedByDescending { item ->
            when (item) {
                is Action -> {
                    try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.dataFim)?.time ?: 0L
                    } catch (e: Exception) { 0L }
                }
                is Activitie -> {
                    try {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.dataFim)?.time ?: 0L
                    } catch (e: Exception) { 0L }
                }
                else -> 0L
            }
        }

        historicoAdapter.updateList(sortedList)
    }

    // Método auxiliar para verificar se um item (Ação ou Atividade) está atrasado
    private fun isItemAtrasada(item: Any): Boolean {
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return when (item) {
            is Action -> {
                // Verifica se alguma atividade da ação está atrasada
                val atividadesDaAcao = actionRepository.getActivitiesByActionId(item.id)
                atividadesDaAcao.any { it.status != StatusAtividade.CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }
            }
            is Activitie -> {
                // Verifica se a atividade está atrasada
                item.status != StatusAtividade.CONCLUIDA && dateFormat.parse(item.dataFim)?.before(hoje) == true
            }
            else -> false
        }
    }

    override fun onItemClick(item: Any) {
        // Tratar clique no item (abrir detalhes de Ação ou Atividade)
        when (item) {
            is Action -> {
                // Abrir tela de detalhes da Ação
                val intent = Intent(this, ActionDetailActivity::class.java)
                intent.putExtra("action_id", item.id)
                startActivity(intent)
            }
            is Activitie -> {
                // Abrir tela de detalhes da Atividade
                val intent = Intent(this, ActivitieDetailActivity::class.java)
                intent.putExtra("activitie_id", item.id)
                startActivity(intent)
            }
        }
    }
} 