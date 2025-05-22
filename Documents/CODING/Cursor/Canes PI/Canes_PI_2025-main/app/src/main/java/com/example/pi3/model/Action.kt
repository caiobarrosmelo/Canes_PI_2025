package com.example.pi3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Action(
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val responsavel: String,
    val orcamento: Double,
    val dataInicio: String,
    val dataFim: String,
    val pilar: String,
    val aprovada: Boolean = false
) : Parcelable