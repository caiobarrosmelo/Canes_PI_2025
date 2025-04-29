package com.example.pi3.coordenador

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.adapters.ActionApprovedAdapter
import com.example.pi3.adapters.setupPilarSpinner
import com.example.pi3.data.ActionRepository


class HomeCoordenadorFragment : Fragment(), OnActionClickListener {

    private lateinit var spinnerPilar: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: ActionRepository
    private lateinit var btnRegistrar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_coordenador, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerPilar = view.findViewById(R.id.spinnerPilar)
        recyclerView = view.findViewById(R.id.recyclerViewApproved)
        btnRegistrar = view.findViewById(R.id.btnRegistrar)
        repository = ActionRepository(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configuração do spinner de Pilar
        setupPilarSpinner(requireContext(), spinnerPilar) { pilarSelecionado ->
            carregarAcoesAprovadas(pilarSelecionado)
        }

        // Configuração do botão de registrar
        btnRegistrar.setOnClickListener {
            val pilarSelecionado = spinnerPilar.selectedItem.toString()
            val action = HomeCoordenadorFragmentDirections
                .actionHomeCoordenadorToRegistrar(pilarSelecionado)
            findNavController().navigate(action)
        }


    }

    private fun carregarAcoesAprovadas(pilar: String) {
        val acoes = repository.getApprovedActionsByPillar(pilar)
        recyclerView.adapter = ActionApprovedAdapter(acoes, repository, this)
    }

    override fun onEditarAcaoClicked(acaoId: Long) {
        val pilarSelecionado = spinnerPilar.selectedItem.toString()
        val action = HomeCoordenadorFragmentDirections
            .actionHomeCoordenadorToEditAction(pilarSelecionado, acaoId)
        findNavController().navigate(action)
    }



}

