package com.example.pi3.model

data class Activitie(
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val responsavel: String,
    val orcamento: Double,
    val dataInicio: String,
    val dataFim: String,
    var status: Boolean = false,
    val aprovada: Boolean = false,
    val acaoId: Long
)