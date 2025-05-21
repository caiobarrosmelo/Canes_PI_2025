package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.coordenador.Actions.OnClickToActionDetailsListener
import com.example.pi3.coordenador.Actions.OnClickToEditActionListener
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action

class ActionApprovedAdapter(
    private val actions: List<Action>,
    private val repository: ActionRepository,
    private val listenerEdit: OnClickToEditActionListener,
    private val listenerActivitieDetails: OnClickToActionDetailsListener,
) : RecyclerView.Adapter<ActionApprovedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val responsavel: TextView = view.findViewById(R.id.txtResponsavel)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        val progresso: ProgressBar = view.findViewById(R.id.progressBar)
        private val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)


        fun bind(action: Action) {
            titulo.text = action.titulo
            responsavel.text = action.responsavel
            dataFim.text = action.dataFim

            val atividades = repository.getActivitiesByActionId(action.id)
            val total = atividades.size
            val concluidas = atividades.count { it.status }
            val progressoPercentual = if (total > 0) (concluidas * 100 / total) else 0
            progresso.progress = progressoPercentual

            itemView.setOnClickListener {
                listenerActivitieDetails.onDetailsActionClicked(action.id)
            }

            btnEditar.setOnClickListener {
                listenerEdit.onEditarAcaoClicked(action.id)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_action_approved, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actions[position])
    }

    override fun getItemCount(): Int = actions.size
}
