package com.example.pi3.coordenador.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActivitieUnapprovedAdapter

import com.example.pi3.data.ActivitieRepository
import com.example.pi3.model.Activitie


class ActivitiesUnapprovedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivitieUnapprovedAdapter
    private lateinit var repository: ActivitieRepository



    private val atividades = mutableListOf<Activitie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities_unapproved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewUnaApproved)


        repository = ActivitieRepository(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializa o adapter com lista vazia
        adapter = ActivitieUnapprovedAdapter(
            activities = atividades,
            onAprovarClick = { activitie ->
                if (repository.aprovarAtividade(activitie.id)) {
                    Toast.makeText(requireContext(), "Atividade aprovada", Toast.LENGTH_SHORT).show()
                    atividades.remove(activitie)
                    adapter.notifyDataSetChanged()
                }
            },
            onRecusarClick = { activitie ->
                if (repository.desaprovarAtividade(activitie.id)) {
                    Toast.makeText(requireContext(), "Atividade recusada", Toast.LENGTH_SHORT).show()
                    atividades.remove(activitie)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        recyclerView.adapter = adapter

loadAtividades()


    }


    private fun loadAtividades() {
        val atividadesNaoAprovadas = repository.getUnapprovedActivities()
        atividades.clear() // Limpa a lista atual
        atividades.addAll(atividadesNaoAprovadas) // Adiciona as novas atividades
        adapter.notifyDataSetChanged()

    }



}