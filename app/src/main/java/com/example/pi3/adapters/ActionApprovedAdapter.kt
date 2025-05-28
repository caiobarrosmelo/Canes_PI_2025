package com.example.pi3.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.coordenador.Actions.ExpiringActivitiesAdapter
import com.example.pi3.listeners.OnClickToActionDetailsListener
import com.example.pi3.data.ActionRepository
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie

class ActionApprovedAdapter(
    private val actions: List<Action>,
    private val repository: ActionRepository,
    private val activitieRepository: ActivitieRepository,
    private val listenerActivitieDetails: OnClickToActionDetailsListener,
    ) : RecyclerView.Adapter<ActionApprovedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val responsavel: TextView = view.findViewById(R.id.txtResponsavel)
        val dataFim: TextView = view.findViewById(R.id.txtDataFim)
        val progresso: ProgressBar = view.findViewById(R.id.progressBar)
        val btnExpiringActivities: ImageButton = view.findViewById(R.id.btnExpiringActivities)


        fun bind(action: Action) {
            titulo.text = action.titulo
            responsavel.text = action.responsavel
            dataFim.text = action.dataFim

            val atividades = repository.getActivitiesByActionId(action.id)
            val total = atividades.size
            val concluidas = atividades.count { it.status == Activitie.STATUS_CONCLUIDA }
            val progressoPercentual = if (total > 0) (concluidas * 100 / total) else 0
            progresso.progress = progressoPercentual

            val expiringActivities = activitieRepository.getExpiringActivitiesByActionId(action.id)
            btnExpiringActivities.visibility = if (expiringActivities.isNotEmpty()) View.VISIBLE else View.GONE



            itemView.setOnClickListener {
                listenerActivitieDetails.onDetailsActionClicked(action.id)
            }

            btnExpiringActivities.setOnClickListener {
                showExpiringActivitiesDialog(action.id)
            }

        }
        private fun showExpiringActivitiesDialog(acaoId: Long) {
            val context = itemView.context
            val dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_expiring_activities, null)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewExpiringActivities)
            val btnClose = dialogView.findViewById<Button>(R.id.btnCloseDialog)

            val expiringActivities = activitieRepository.getExpiringActivitiesByActionId(acaoId)

            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = ExpiringActivitiesAdapter(expiringActivities, activitieRepository)
            recyclerView.adapter = adapter

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)
                .create()

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            if (expiringActivities.isEmpty()) {
                dialogView.findViewById<TextView>(R.id.txtDialogTitle).text =
                    "Nenhuma atividade expira em 30 dias"
            }

            dialog.show()
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
