package com.example.pi3.apoio.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActivityApprovedCompleteAdapter
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.data.ActivitieRepository

class ActivityViewApoio : function_arrow_back() {
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

        val args = ActivityViewApoioArgs.fromBundle(requireArguments())
        activityId = args.activityId

        recyclerViewActivity.layoutManager = LinearLayoutManager(requireContext())

        carregarAtividade(activityId)
    }

    private fun carregarAtividade(activityId: Long) {
        val atividade = repositoryActivity.getActivityById(activityId)
        if (atividade != null) {
            // Cria o adaptador sem listener de exclusão
            val activityAdapter = ActivityApprovedCompleteAdapter(atividade, null)
            recyclerViewActivity.adapter = activityAdapter
        } else {
            Toast.makeText(requireContext(), "Atividade não encontrada", Toast.LENGTH_SHORT).show()
        }
    }
}
