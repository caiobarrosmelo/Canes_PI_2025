package com.example.pi3.model

data class Activitie(
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val responsavel: String,
    val orcamento: Double,
    val dataInicio: String,
    val dataFim: String,
    var status: Int = STATUS_EM_ANDAMENTO,
    val aprovada: Boolean = false,
    val acaoId: Long
) {
    companion object {
        const val STATUS_EM_ANDAMENTO = 0
        const val STATUS_CONCLUIDA = 1
        const val STATUS_ATRASADA = 2
    }
}