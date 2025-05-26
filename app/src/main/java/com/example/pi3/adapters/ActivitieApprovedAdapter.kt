package com.example.pi3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.listeners.OnActivityStatusChangedListener
import com.example.pi3.listeners.OnDetailsActivityClicked
import com.example.pi3.model.Activitie

class ActivitieApprovedAdapter(
    private val activities: List<Activitie>,
    private val repository: ActivitieRepository,
    private val statusChangedListener: OnActivityStatusChangedListener? = null,
    private val listener: OnDetailsActivityClicked? = null
): RecyclerView.Adapter<ActivitieApprovedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        private val box: CheckBox = view.findViewById(R.id.checkboxCompleted)

        fun bind(activitie: Activitie, position: Int) {
            titulo.text = activitie.titulo
            box.isChecked = activitie.status == Activitie.STATUS_CONCLUIDA

            box.setOnCheckedChangeListener { _, isChecked ->
                val currentActivitie = activities[position]
                val newStatus = if (isChecked) Activitie.STATUS_CONCLUIDA else Activitie.STATUS_EM_ANDAMENTO

                if (repository.atualizarStatusAtividade(currentActivitie.id, newStatus)) {
                    Toast.makeText(
                        itemView.context,
                        "${currentActivitie.titulo} marcada como ${if (isChecked) "concluída" else "não concluída"}",
                        Toast.LENGTH_SHORT
                    ).show()
                    statusChangedListener?.onActivityStatusChanged()
                }
            }

            itemView.setOnClickListener {
                listener?.onActivityViewClicked(activitie.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activitie_approved, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position], position)
    }

    override fun getItemCount(): Int = activities.size
}