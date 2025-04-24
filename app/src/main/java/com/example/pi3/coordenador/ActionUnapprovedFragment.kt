package com.example.pi3.coordenador

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActionUnapprovedAdapter
import com.example.pi3.adapters.setupPilarSpinner
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action


class ActionUnapprovedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActionUnapprovedAdapter
    private lateinit var repository: ActionRepository
    private lateinit var spinner: Spinner

    // Lista mutável para manter referência e poder atualizar
    private val acoes = mutableListOf<Action>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_unapproved_coordenador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewUnaApproved)
        spinner = view.findViewById(R.id.spinnerPilar)

        repository = ActionRepository(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializa o adapter com lista vazia
        adapter = ActionUnapprovedAdapter(
            actions = acoes,
            onAprovarClick = { action ->
                if (repository.aprovarAcao(action.id)) {
                    Toast.makeText(requireContext(), "Ação aprovada", Toast.LENGTH_SHORT).show()
                    acoes.remove(action)
                    adapter.notifyDataSetChanged()
                }
            },
            onRecusarClick = { action ->
                if (repository.recusarAcao(action.id)) {
                    Toast.makeText(requireContext(), "Ação recusada", Toast.LENGTH_SHORT).show()
                    acoes.remove(action)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        recyclerView.adapter = adapter

        // Configura o spinner usando a função reutilizável
        setupPilarSpinner(requireContext(), spinner) { pilarSelecionado ->
            // Atualiza a lista com as ações não aprovadas do pilar escolhido
            val novasAcoes = repository.getUnapprovedActionsByPillar(pilarSelecionado)
            Log.d("ActionUnapprovedFragment", "Ações recuperadas: ${novasAcoes.size} ações")
            // Verifique se novasAcoes não está vazia
            if (novasAcoes.isEmpty()) {
                Toast.makeText(requireContext(), "Nenhuma ação encontrada", Toast.LENGTH_SHORT)
                    .show()
            } else {
                adapter.updateData(novasAcoes)  // Atualiza a lista usando o método do adapter
            }
        }
    }

}