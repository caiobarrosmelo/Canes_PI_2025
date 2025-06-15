package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.listeners.OnClickToEditActionListener
import com.example.pi3.data.ActionRepository
import com.example.pi3.listeners.OnActivityStatusChangedListener
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie


//Componente completo da ação
class ActionApprovedCompleteAdapter(
     private val action: Action,
     private val repository: ActionRepository,
     private val listener: OnClickToEditActionListener

) : RecyclerView.Adapter<ActionApprovedCompleteAdapter.ViewHolder>(),
    OnActivityStatusChangedListener {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val responsavel: TextView = view.findViewById(R.id.txtResponsavel)
        val descricao: TextView = view.findViewById(R.id.txtDescricao)
        val orcamento: TextView = view.findViewById(R.id.txtValueOrcamento)
        val dataInicio: TextView = view.findViewById(R.id.txtDataInicio)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        val progresso: ProgressBar = view.findViewById(R.id.progressBar)
        private val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)

        fun bind(action: Action) {
            titulo.text = action.titulo
            descricao.text = action.descricao
            responsavel.text = action.responsavel
            orcamento.text = action.orcamento.toString()
            dataInicio.text = action.dataInicio
            dataFim.text = action.dataFim
            updateProgress()
            btnEditar.setOnClickListener {
                listener.onEditarAcaoClicked(action.id)
            }
        }

        //lógica da barra de progresso
        fun updateProgress() {
            val atividades = repository.getActivitiesByActionId(action.id)
            val total = atividades.size
            val concluidas = atividades.count { it.status == Activitie.STATUS_CONCLUIDA }
            val progressoPercentual = if (total > 0) (concluidas * 100 / total) else 0

            //Animação suave
            val currentProgress = progresso.progress
            val animation = ObjectAnimator.ofInt(progresso, "progress", currentProgress, progressoPercentual)
            animation.duration = 750
            animation.interpolator = DecelerateInterpolator()
            animation.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_action_approved_complete_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(action)
    }

    override fun onActivityStatusChanged() {
        notifyDataSetChanged() // Re-renderiza o item para atualizar o progresso
    }
}