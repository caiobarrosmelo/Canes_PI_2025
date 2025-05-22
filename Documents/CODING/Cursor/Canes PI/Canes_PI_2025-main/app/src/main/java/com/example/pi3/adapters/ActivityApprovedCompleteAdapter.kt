package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.listeners.OnDeleteActivity
import com.example.pi3.model.Activitie

class ActivityApprovedCompleteAdapter(
    private val activity: Activitie,
    private val listener: OnDeleteActivity? = null // Torna opcional
) : RecyclerView.Adapter<ActivityApprovedCompleteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val descricao: TextView = view.findViewById(R.id.txtDescricao)
        val responsavel: TextView = view.findViewById(R.id.txtResponsavel)
        val orcamento: TextView = view.findViewById(R.id.txtValueOrcamento)
        val dataInicio: TextView = view.findViewById(R.id.txtDataInicio)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        private val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)

        fun bind(activity: Activitie) {
            titulo.text = activity.titulo
            descricao.text = activity.descricao
            responsavel.text = activity.responsavel
            orcamento.text = activity.orcamento.toString()
            dataInicio.text = activity.dataInicio
            dataFim.text = activity.dataFim

            // ✅ Só mostra o botão de editar se o listener estiver presente
            if (listener != null) {
                btnEditar.visibility = View.VISIBLE
                btnEditar.setOnClickListener {
                    listener.onDeleteActivityClicked(activity.id)
                }
            } else {
                btnEditar.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_activity_approved_complete_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder
    }
}
