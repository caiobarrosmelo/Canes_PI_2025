package com.example.pi3.gestor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.model.Activitie

class HistoricoAdapter(private var activities: List<Activitie>, private val listener: OnActivitieClickListener) : RecyclerView.Adapter<HistoricoAdapter.ViewHolder>() {

    interface OnActivitieClickListener {
        fun onActivitieClick(activitieId: Long)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusIndicator: View = view.findViewById(R.id.statusIndicator)
        val textViewActivitieTitle: TextView = view.findViewById(R.id.textViewActivitieTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activitie = activities[position]
        holder.textViewActivitieTitle.text = activitie.titulo

        // Configurar a cor do indicador de status
        val color = when (activitie.status) {
            Activitie.STATUS_CONCLUIDA -> Color.GREEN
            Activitie.STATUS_ATRASADA -> Color.RED
            else -> Color.YELLOW // Em andamento ou outro status
        }
        val drawable = holder.statusIndicator.background as GradientDrawable
        drawable.setColor(color)

        // Configurar listener de clique
        holder.itemView.setOnClickListener {
            listener.onActivitieClick(activitie.id)
        }
    }

    override fun getItemCount(): Int = activities.size

    fun updateList(newList: List<Activitie>) {
        activities = newList
        notifyDataSetChanged()
    }
} 