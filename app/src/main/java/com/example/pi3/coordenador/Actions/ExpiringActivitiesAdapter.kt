package com.example.pi3.coordenador.Actions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pi3.R
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.model.Activitie
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ExpiringActivitiesAdapter(
    private val activities: List<Activitie>,
    private val repository: ActivitieRepository
) : RecyclerView.Adapter<ExpiringActivitiesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtActivityTitle: TextView = itemView.findViewById(R.id.txtActivityTitle)
        val txtExpirationDays: TextView = itemView.findViewById(R.id.txtExpirationDays)

        fun bind(activity: Activitie) {
            txtActivityTitle.text = activity.titulo ?: "Sem título"
            val daysRemaining = calculateDaysRemaining(activity.dataFim)
            txtExpirationDays.text = "Expira em $daysRemaining dias"
        }

        private fun calculateDaysRemaining(dateString: String): Long {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return try {
                val endDate = dateFormat.parse(dateString)
                val currentDate = Date()
                if (endDate != null) {
                    val diffInTime = endDate.time - currentDate.time
                    TimeUnit.DAYS.convert(diffInTime, TimeUnit.MILLISECONDS)
                } else {
                    -1 // Indicate error or invalid date
                }
            } catch (e: Exception) {
                -1 // Indicate error
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expiring_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}