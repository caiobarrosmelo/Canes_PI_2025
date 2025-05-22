package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface OnActionClickListener {
    fun onActionClick(action: Action)
}

class HistoricoActionAdapter(private var actions: List<Action>, private val listener: OnActionClickListener) :
    RecyclerView.Adapter<HistoricoActionAdapter.HistoricoViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewActionTitle: TextView = itemView.findViewById(R.id.textViewActionTitle)
        val textViewActionDate: TextView = itemView.findViewById(R.id.textViewActionDate)
        val statusIndicator: View = itemView.findViewById(R.id.statusIndicator)
        val textViewActionResponsible: TextView = itemView.findViewById(R.id.textViewActionResponsible)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onActionClick(actions[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico_action, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val action = actions[position]
        holder.textViewActionTitle.text = action.titulo
        holder.textViewActionResponsible.text = action.responsavel
        holder.textViewActionDate.text = action.dataFim

        val dataAtual = Date()
        val dataFim = try { dateFormat.parse(action.dataFim) } catch (e: Exception) { null }

        if (action.aprovada && dataFim != null && !dataFim.after(dataAtual)) {
            holder.statusIndicator.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.status_indicator_green)
        } else if (dataFim != null && dataFim.after(dataAtual)) {
            holder.statusIndicator.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.status_indicator_yellow)
        } else {
             holder.statusIndicator.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.status_indicator_red)
        }
    }

    override fun getItemCount(): Int = actions.size

    fun updateData(newList: List<Action>) {
        actions = newList
        notifyDataSetChanged()
    }
} 