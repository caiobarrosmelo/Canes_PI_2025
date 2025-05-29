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

class HistoricoActivity : AppCompatActivity(), HistoricoAdapter.OnItemClickListener {

    private lateinit var spinnerFilterType: Spinner
    private lateinit var spinnerFilterStatus: Spinner
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
    }

    private fun setupRecyclerView() {
        historicoAdapter = HistoricoAdapter(listOf(), this) // 'this' refere-se à OnItemClickListener
        recyclerViewHistorico.layoutManager = LinearLayoutManager(this)
        recyclerViewHistorico.adapter = historicoAdapter
    }

    private fun loadAllItems() {
        val allActivities = activitieRepository.getAllApprovedActivities() // Buscar todas atividades aprovadas
        val allActions = actionRepository.getAllActions() // Assumindo que existe um método para buscar todas as ações

        // Combinar Ações e Atividades. Talvez seja necessário adicionar uma flag ou tipo para diferenciar.
        // Por enquanto, vamos apenas combiná-los. O adaptador precisará lidar com os dois tipos.
        allItems = allActions + allActivities
        filterItems() // Aplicar filtros iniciais
    }

    private fun filterItems() {
        val selectedType = spinnerFilterType.selectedItem.toString()
        val selectedStatus = spinnerFilterStatus.selectedItem.toString()

        val filteredList = allItems.filter { item ->
            val typeMatch = when (selectedType) {
                "Histórico Geral" -> true
                "Histórico de Ações" -> item is Action
                "Histórico de Atividades" -> item is Activitie
                else -> false
            }

            val statusMatch = when (selectedStatus) {
                "Tudo" -> true
                "Concluída" -> if (item is Activitie) item.status == StatusAtividade.CONCLUIDA else false // Ações não tem status 'Concluída'
                "Em andamento" -> if (item is Activitie) item.status == StatusAtividade.EM_PROCESSO else true // Ações 'em andamento' sempre aparecem em 'Em andamento'
                "Atrasada" -> if (item is Activitie) item.status == Activitie.STATUS_ATRASADA else false // Assumindo status atrasada para atividade
                else -> false
            }

            typeMatch && statusMatch
        }

        // Ordenar a lista combinada por data (mais recente primeiro)
        val sortedList = filteredList.sortedByDescending { item ->
            when (item) {
                is Action -> {
                    // Converter data de String para comparável (assumindo formato yyyy-MM-dd)
                     try {
                         SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.dataFim)?.time ?: 0L
                     } catch (e: Exception) { 0L }
                }
                is Activitie -> {
                     // Converter data de String para comparável (assumindo formato yyyy-MM-dd)
                     try {
                         SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.dataFim)?.time ?: 0L
                     } catch (e: Exception) { 0L }
                }
                else -> 0L
            }
        }

        historicoAdapter.updateList(sortedList)
    }

    override fun onItemClick(item: Any) {
        // Tratar clique no item (abrir detalhes de Ação ou Atividade)
        when (item) {
            is Action -> {
                // Abrir tela de detalhes da Ação (se existir)
                // val intent = Intent(this, ActionDetailActivity::class.java)
                // intent.putExtra("action_id", item.id)
                // startActivity(intent)
                // Toast.makeText(this, "Clique na Ação: ${item.titulo}", Toast.LENGTH_SHORT).show()
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