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
import com.example.pi3.model.Activitie

class HistoricoActivity : AppCompatActivity(), HistoricoAdapter.OnActivitieClickListener {

    private lateinit var spinnerFilter: Spinner
    private lateinit var recyclerViewHistorico: RecyclerView
    private lateinit var activitieRepository: ActivitieRepository
    private lateinit var historicoAdapter: HistoricoAdapter
    private var allActivities: List<Activitie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        spinnerFilter = findViewById(R.id.spinnerFilter)
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistorico)
        activitieRepository = ActivitieRepository(this)

        setupSpinner()
        setupRecyclerView()
        loadActivities()
    }

    private fun setupSpinner() {
        val filterOptions = arrayOf("Todos", "Concluídas", "Em andamento", "Atrasadas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filterOptions)
        spinnerFilter.adapter = adapter

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterActivities(filterOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não faz nada
            }
        }
    }

    private fun setupRecyclerView() {
        historicoAdapter = HistoricoAdapter(listOf(), this) // 'this' refere-se à OnActivitieClickListener
        recyclerViewHistorico.layoutManager = LinearLayoutManager(this)
        recyclerViewHistorico.adapter = historicoAdapter
    }

    private fun loadActivities() {
        // Carregar todas as atividades aprovadas
        allActivities = activitieRepository.getAllApprovedActivities()
        historicoAdapter.updateList(allActivities)
    }

    private fun filterActivities(statusFilter: String) {
        val filteredList = when (statusFilter) {
            "Concluídas" -> allActivities.filter { it.status == Activitie.STATUS_CONCLUIDA }
            "Em andamento" -> allActivities.filter { it.status == Activitie.STATUS_EM_ANDAMENTO }
            "Atrasadas" -> allActivities.filter { it.status == Activitie.STATUS_ATRASADA }
            else -> allActivities // "Todos"
        }
        historicoAdapter.updateList(filteredList)
    }

    override fun onActivitieClick(activitieId: Long) {
        // Abrir tela de detalhes da atividade
        val intent = Intent(this, ActivitieDetailActivity::class.java)
        intent.putExtra("activitie_id", activitieId)
        startActivity(intent)
    }

    // Adicionar método getAllApprovedActivities ao ActivitieRepository
    // (será feito no próximo passo se necessário, mas a chamada está aqui)
} 