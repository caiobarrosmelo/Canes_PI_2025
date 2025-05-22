package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.model.Action

class ActionUnapprovedAdapter(
    private val actions: MutableList<Action>,
    private val onAprovarClick: (Action) -> Unit,
    private val onRecusarClick: (Action) -> Unit
) : RecyclerView.Adapter<ActionUnapprovedAdapter.ActionViewHolder>() {

    inner class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_action_unapproved, parent, false)
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actions[position]
        holder.titulo.text = action.titulo
        holder.descricao.text = action.descricao
        holder.atribuicao.text = "Responsável:"
        holder.nome.text = action.responsavel
        holder.orcamento.text = "R$ %.2f".format(action.orcamento)
        holder.inicio.text = action.dataInicio
        holder.fim.text = action.dataFim

        holder.btnAprovar.setOnClickListener { onAprovarClick(action) }
        holder.btnRecusar.setOnClickListener { onRecusarClick(action) }
    }

    override fun getItemCount(): Int = actions.size

    fun updateData(newList: List<Action>) {
        actions.clear()
        actions.addAll(newList)
        notifyDataSetChanged()
    }
    fun getCurrentList(): List<Action> = actions


}