package com.example.pi3.coordenador.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.data.StatusAtividade
import com.example.pi3.adapters.ActivitieUnapprovedAdapter
import com.example.pi3.model.Activitie

class ActivitiesUnapprovedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerTipoAtividade: Spinner
    private lateinit var repository: ActivitieRepository
    private lateinit var adapter: ActivitieUnapprovedAdapter
    private var modoAtual = ModoVisualizacao.APROVACAO_INICIAL

    enum class ModoVisualizacao {
        APROVACAO_INICIAL, APROVACAO_CONCLUSAO
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities_unapproved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewUnaApproved)
        spinnerTipoAtividade = view.findViewById(R.id.spinnerTipoAtividade)
        repository = ActivitieRepository(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActivitieUnapprovedAdapter(
            activities = mutableListOf(),
            onAprovarClick = { activitie ->
                when (modoAtual) {
                    ModoVisualizacao.APROVACAO_INICIAL -> {
                        if (repository.aprovarAtividadeInicial(activitie.id)) {
                            Toast.makeText(requireContext(), "Atividade aprovada inicialmente", Toast.LENGTH_SHORT).show()
                            carregarAtividadesPendentesAprovacaoInicial()
                        }
                    }
                    ModoVisualizacao.APROVACAO_CONCLUSAO -> {
                        if (repository.aprovarConclusaoAtividade(activitie.id)) {
                            Toast.makeText(requireContext(), "Conclusão da atividade aprovada", Toast.LENGTH_SHORT).show()
                            carregarAtividadesPendentesAprovacaoConclusao()
                        }
                    }
                }
            },
            onRecusarClick = { activitie ->
                when (modoAtual) {
                    ModoVisualizacao.APROVACAO_INICIAL -> {
                        if (repository.desaprovarAtividadeInicial(activitie.id)) {
                            Toast.makeText(requireContext(), "Atividade rejeitada inicialmente", Toast.LENGTH_SHORT).show()
                            carregarAtividadesPendentesAprovacaoInicial()
                        }
                    }
                    ModoVisualizacao.APROVACAO_CONCLUSAO -> {
                        if (repository.rejeitarConclusaoAtividade(activitie.id)) {
                            Toast.makeText(requireContext(), "Conclusão da atividade rejeitada", Toast.LENGTH_SHORT).show()
                            carregarAtividadesPendentesAprovacaoConclusao()
                        }
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        setupSpinner()
    }

    private fun setupSpinner() {
        val tiposAtividade = arrayOf("Aprovação Inicial de Atividades", "Aprovação de Conclusão de Atividades")
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tiposAtividade
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerTipoAtividade.adapter = spinnerAdapter
        spinnerTipoAtividade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                modoAtual = when (position) {
                    0 -> ModoVisualizacao.APROVACAO_INICIAL
                    1 -> ModoVisualizacao.APROVACAO_CONCLUSAO
                    else -> ModoVisualizacao.APROVACAO_INICIAL
                }
                atualizarListaPorModo()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                modoAtual = ModoVisualizacao.APROVACAO_INICIAL
                atualizarListaPorModo()
            }
        }
    }

    private fun atualizarListaPorModo() {
        when (modoAtual) {
            ModoVisualizacao.APROVACAO_INICIAL -> carregarAtividadesPendentesAprovacaoInicial()
            ModoVisualizacao.APROVACAO_CONCLUSAO -> carregarAtividadesPendentesAprovacaoConclusao()
        }
    }

    private fun carregarAtividadesPendentesAprovacaoInicial() {
        val atividades = repository.getActivitiesPendingApproval()
        adapter.atualizarLista(atividades)
    }

    private fun carregarAtividadesPendentesAprovacaoConclusao() {
        val atividades = repository.getActivitiesPendingCompletionApproval()
        adapter.atualizarLista(atividades)
    }

    override fun onResume() {
        super.onResume()
        atualizarListaPorModo()
    }
}