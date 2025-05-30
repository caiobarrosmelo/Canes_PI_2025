package com.example.pi3.gestor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.StatusAtividade
import com.example.pi3.model.Activitie
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricoAdapter(private var items: List<Any>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Any)
    }

    private val VIEW_TYPE_ACTION = 0
    private val VIEW_TYPE_ACTIVITIE = 1

    inner class ActionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val tipo: TextView = view.findViewById(R.id.txtTipo)

        fun bind(action: Action) {
            titulo.text = action.titulo
            dataFim.text = action.dataFim
            tipo.text = "Ação"

            // Lógica de status para Ações (usando o campo status temporário)
            val color = when (action.status) {
                StatusAtividade.CONCLUIDA -> Color.parseColor("#006400") // Verde escuro
                StatusAtividade.EM_PROCESSO -> Color.parseColor("#FFC107") // Amarelo escuro
                // Assumindo que Ações não tem status de rejeição/atraso direto, mas podemos adicionar se necessário
                else -> Color.GRAY // Cor padrão para outros status (incluindo 0/PENDENTE_APROVACAO_INICIAL temporário)
            }
            val drawable = statusIndicator.background as GradientDrawable
            drawable.setColor(color)

            itemView.setOnClickListener {
                listener.onItemClick(action)
            }
        }
    }

    inner class ActivitieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val tipo: TextView = view.findViewById(R.id.txtTipo)

        fun bind(activitie: Activitie) {
            titulo.text = activitie.titulo ?: "Sem título"
            dataFim.text = activitie.dataFim
            tipo.text = "Atividade"

            // Configurar a cor do indicador de status para Atividades
            val color = when (activitie.status) {
                StatusAtividade.CONCLUIDA -> Color.parseColor("#006400") // Verde escuro
                StatusAtividade.REJEITADA_CONCLUSAO, StatusAtividade.REJEITADA_INICIAL -> Color.parseColor("#8B0000") // Vermelho escuro
                StatusAtividade.EM_PROCESSO, StatusAtividade.PENDENTE_APROVACAO_INICIAL, StatusAtividade.PENDENTE_APROVACAO_CONCLUSAO -> Color.parseColor("#FFC107") // Amarelo escuro
                // Verificar se há um status para Atrasada na classe Activitie model
                Activitie.STATUS_ATRASADA -> Color.parseColor("#8B0000") // Vermelho escuro
                else -> Color.GRAY // Cor padrão para outros status
            }
            val drawable = statusIndicator.background as GradientDrawable
            drawable.setColor(color)

            itemView.setOnClickListener {
                listener.onItemClick(activitie)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Action -> VIEW_TYPE_ACTION
            is Activitie -> VIEW_TYPE_ACTIVITIE
            else -> -1 // Tipo desconhecido
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ACTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_historico, parent, false)
                ActionViewHolder(view)
            }
            VIEW_TYPE_ACTIVITIE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_historico, parent, false)
                ActivitieViewHolder(view)
            }
            else -> throw IllegalArgumentException("Tipo de item desconhecido: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ActionViewHolder -> holder.bind(item as Action)
            is ActivitieViewHolder -> holder.bind(item as Activitie)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<Any>) {
        items = newList
        notifyDataSetChanged()
    }
} 