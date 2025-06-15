package com.example.pi3.coordenador.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActivityApprovedCompleteAdapter
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.listeners.OnDeleteActivity

class ActivityView : function_arrow_back(), OnDeleteActivity {
    private lateinit var recyclerViewActivity: RecyclerView
    private lateinit var repositoryActivity: ActivitieRepository
    private var activityId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        recyclerViewActivity = view.findViewById(R.id.recyclerViewActivity)
        repositoryActivity = ActivitieRepository(requireContext())

        val args = ActivityViewArgs.fromBundle(requireArguments())
        activityId = args.activityId

        recyclerViewActivity.layoutManager = LinearLayoutManager(requireContext())

        carregarAtividade(activityId)

    }

    //função para carregar as atividades
    private fun carregarAtividade(activityId: Long) {
        val atividade = repositoryActivity.getActivityById(activityId)
        if (atividade != null) {
            // Cria o adaptador de ação
            val activityAdapter = ActivityApprovedCompleteAdapter(atividade, this)
            recyclerViewActivity.adapter = activityAdapter

} else {
            Toast.makeText(requireContext(), "Atividade não encontrada", Toast.LENGTH_SHORT).show()
        }
    }

//função para deletar as atividades
    override fun onDeleteActivityClicked(activityId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir esta atividade?")
            .setPositiveButton("Excluir") { _, _ ->
                delete(activityId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun delete(activityId: Long) {
        val success = repositoryActivity.deleteActivityById(activityId)
        if (success) {
            Toast.makeText(requireContext(), "Atividade excluída com sucesso", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp() // Voltar para ActivitiesApproved
        } else {
            Toast.makeText(requireContext(), "Erro ao excluir atividade", Toast.LENGTH_SHORT).show()
        }
    }

    }



