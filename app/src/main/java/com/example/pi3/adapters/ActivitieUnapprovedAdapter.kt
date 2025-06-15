package com.example.pi3.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie


//Componente das atividades ainda não aprovadas
class ActivitieUnapprovedAdapter(

    private val activities: MutableList<Activitie>,
    private val onAprovarClick: (Activitie) -> Unit,
    private val onRecusarClick: (Activitie) -> Unit
) : RecyclerView.Adapter<ActivitieUnapprovedAdapter.ActivitieViewHolder>() {

    inner class ActivitieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val descricao: TextView = itemView.findViewById(R.id.txtDescricao)
        val atribuicao: TextView = itemView.findViewById(R.id.txtAtribuicao)
        val nome: TextView = itemView.findViewById(R.id.txtNome)
        val orcamento: TextView = itemView.findViewById(R.id.txtValueOrcamento)
        val inicio: TextView = itemView.findViewById(R.id.txtDataInicio)
        val fim: TextView = itemView.findViewById(R.id.txtDataFim)
        val btnAprovar: Button = itemView.findViewById(R.id.btnAprovar)
        val btnRecusar: Button = itemView.findViewById(R.id.btnRecusar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activitie_unapproved, parent, false)
        return ActivitieViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivitieViewHolder, position: Int) {
        val activitie = activities[position]
        holder.titulo.text = activitie.titulo
        holder.descricao.text = activitie.descricao
        holder.atribuicao.text = "Responsável:"
        holder.nome.text = activitie.responsavel
        holder.orcamento.text = "R$ %.2f".format(activitie.orcamento)
        holder.inicio.text = activitie.dataInicio
        holder.fim.text = activitie.dataFim

        holder.btnAprovar.setOnClickListener { onAprovarClick(activitie) }
        holder.btnRecusar.setOnClickListener { onRecusarClick(activitie) }
    }

    override fun getItemCount(): Int = activities.size

    fun updateData(newList: List<Activitie>) {
        activities.clear()
        activities.addAll(newList)
        notifyDataSetChanged()
    }

    fun atualizarLista(novasAtividades: List<Activitie>) {
        activities.clear()
        activities.addAll(novasAtividades)
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<Activitie> = activities


}