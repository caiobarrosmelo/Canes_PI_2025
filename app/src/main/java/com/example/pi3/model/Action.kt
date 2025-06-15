package com.example.pi3.model

//classe que modula os dados do objeto ação no código
data class Action(
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val responsavel: String,
    val orcamento: Double,
    val dataInicio: String,
    val dataFim: String,
    val pilar: String,
    val aprovada: Boolean = false,
    val status: Int = 0 // Campo de status temporário para compilação
)