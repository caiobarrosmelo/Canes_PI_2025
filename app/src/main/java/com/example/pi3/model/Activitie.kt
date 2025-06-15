package com.example.pi3.model

//classe que modula os dados do objeto atividade no código
data class Activitie(
    val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val responsavel: String,
    val orcamento: Double,
    val dataInicio: String,
    val dataFim: String,
    var status: Int = PENDENTE_APROVACAO_INICIAL,
    val aprovada: Boolean = false,
    val acaoId: Long
) {
    companion object {
        const val PENDENTE_APROVACAO_INICIAL = 0
        const val STATUS_EM_ANDAMENTO = 1
        const val STATUS_CONCLUIDA = 3
        const val STATUS_ATRASADA = 2
    }
}