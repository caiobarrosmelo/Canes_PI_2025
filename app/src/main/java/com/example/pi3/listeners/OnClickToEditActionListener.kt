package com.example.pi3.listeners

//listener para navegar até a pagina onde a ação pode ser editada
interface OnClickToEditActionListener {
    fun onEditarAcaoClicked(acaoId: Long)
}