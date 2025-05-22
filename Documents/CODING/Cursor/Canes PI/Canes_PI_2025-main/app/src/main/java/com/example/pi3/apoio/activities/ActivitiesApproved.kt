package com.example.pi3.apoio.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActionApprovedCompleteAdapter
import com.example.pi3.adapters.ActivitieApprovedAdapter
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.coordenador.Actions.EditActionArgs
import com.example.pi3.coordenador.activities.ActivitiesApprovedDirections
import com.example.pi3.listeners.OnClickToEditActionListener
import com.example.pi3.data.ActionRepository
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.listeners.OnDetailsActivityClicked
import com.example.pi3.model.Activitie


class ActivitiesApproved : function_arrow_back(), OnDetailsActivityClicked, OnClickToEditActionListener{

    private lateinit var recyclerViewAction: RecyclerView
    private lateinit var recyclerViewActivity: RecyclerView
    private lateinit var repositoryAction: ActionRepository
    private lateinit var btnRegistrar: Button
    private lateinit var repositoryActivity: ActivitieRepository
    private lateinit var adapter: ActivitieApprovedAdapter
    private var acaoId: Long = -1
    private var pilarSelecionado: String? = null
    private val atividades = mutableListOf<Activitie>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities_approved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        recyclerViewAction = view.findViewById(R.id.recyclerViewActionDetailsApproved)
        recyclerViewActivity = view.findViewById(R.id.recyclerViewApproved)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)
        repositoryAction = ActionRepository(requireContext())
        repositoryActivity = ActivitieRepository(requireContext())


        recyclerViewAction.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewActivity.layoutManager = LinearLayoutManager(requireContext())


        val args = EditActionArgs.fromBundle(requireArguments())
        acaoId = args.acaoId
        pilarSelecionado = args.pilarSelecionado



        adapter = ActivitieApprovedAdapter(atividades, repositoryActivity, null, this)
        recyclerViewActivity.adapter = adapter


        carregarAtividades(acaoId)

        carregarAcao(acaoId)

        btnRegistrar.setOnClickListener {

            val action = ActivitiesApprovedDirections.actionActivitiesApprovedToActivitieRegister(acaoId)
            findNavController().navigate(action)

        }



    }

    override fun onResume() {
        super.onResume()
        carregarAtividades(acaoId)
    }


    private fun carregarAcao(acaoId: Long) {
        val acao = repositoryAction.getActionById(acaoId)
        if (acao != null) {
            // Cria o adaptador de ação
            val actionAdapter = ActionApprovedCompleteAdapter(acao, repositoryAction, this)
            recyclerViewAction.adapter = actionAdapter
            // Atualiza o adaptador de atividades com o actionAdapter como listener
            adapter = ActivitieApprovedAdapter(atividades, repositoryActivity, actionAdapter, this)
            recyclerViewActivity.adapter = adapter
            // Recarrega as atividades para garantir que o adaptador atualizado seja usado
            carregarAtividades(acaoId)
        } else {
            Toast.makeText(requireContext(), "Ação não encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarAtividades(acaoId: Long) {
        val atividadesAprovadas = repositoryActivity.getActivitiesApprovedByActionId(acaoId)
        Log.d("ActivitiesApproved", "Carregando atividades: ${atividadesAprovadas.size} encontradas para acaoId=$acaoId")
        atividadesAprovadas.forEach { atividade ->
            Log.d("ActivitiesApproved", "Atividade ID=${atividade.id}, Título=${atividade.titulo}")
        }
        if (atividadesAprovadas.isNotEmpty()) {
            atividades.clear()
            atividades.addAll(atividadesAprovadas)
            adapter.notifyDataSetChanged()
        } else {
            atividades.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Nenhuma atividade aprovada encontrada", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onEditarAcaoClicked(acaoId: Long) {

        val action = ActivitiesApprovedDirections.actionActivitiesApprovedToEditAction(pilarSelecionado!!, acaoId)
        findNavController().navigate(action)

    }

    override fun onActivityViewClicked(activityId: Long) {

        val action = ActivitiesApprovedDirections.actionActivitiesApprovedToActivityView(activityId)
        findNavController().navigate(action)
    }


}