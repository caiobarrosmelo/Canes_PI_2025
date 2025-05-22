package com.example.pi3.gestor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action
import com.example.pi3.adapters.HistoricoActionAdapter
import com.example.pi3.adapters.OnActionClickListener

class HistoricoActivity : AppCompatActivity(), OnActionClickListener {

    private lateinit var textViewHistoricoTitle: TextView
    private lateinit var btnFilterVisaoGeral: Button
    private lateinit var btnFilterConcluido: Button
    private lateinit var btnFilterPendente: Button
    private lateinit var recyclerViewHistorico: RecyclerView
    private lateinit var actionRepository: ActionRepository
    private lateinit var adapter: HistoricoActionAdapter

    private var fullActionList: List<Action> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        textViewHistoricoTitle = findViewById(R.id.textViewHistoricoTitle)
        btnFilterVisaoGeral = findViewById(R.id.btnFilterVisaoGeral)
        btnFilterConcluido = findViewById(R.id.btnFilterConcluido)
        btnFilterPendente = findViewById(R.id.btnFilterPendente)
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistorico)
        actionRepository = ActionRepository(this)

        // Configurar o RecyclerView
        recyclerViewHistorico.layoutManager = LinearLayoutManager(this)
        // Inicializar o adapter com uma lista vazia por enquanto
        adapter = HistoricoActionAdapter(emptyList(), this) // Passando 'this' como listener
        recyclerViewHistorico.adapter = adapter

        // Carregar a lista completa de ações
        fullActionList = actionRepository.getAllActions()
        displayActions(fullActionList) // Exibir todas as ações inicialmente

        // Configurar listeners para os botões de filtro
        btnFilterVisaoGeral.setOnClickListener { displayActions(fullActionList) }
        btnFilterConcluido.setOnClickListener { filterActions("Concluído") }
        btnFilterPendente.setOnClickListener { filterActions("Pendente") }
    }

    private fun filterActions(status: String) {
        val filteredList = when (status) {
            "Concluído" -> actionRepository.getCompletedActions()
            "Pendente" -> {
                val inProgress = actionRepository.getInProgressActions()
                val late = actionRepository.getLateActions()
                inProgress + late
            }
            else -> fullActionList
        }
        displayActions(filteredList)
    }

    private fun displayActions(actions: List<Action>) {
        adapter.updateData(actions)
    }

    override fun onActionClick(action: Action) {
        val intent = Intent(this, ActionDetailsActivity::class.java)
        intent.putExtra("action", action)
        startActivity(intent)
    }
}