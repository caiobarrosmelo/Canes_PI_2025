package com.example.pi3.coordenador.Actions

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.pi3.R
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action
import java.util.Calendar

class EditAction : Fragment() {

    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var edtTitulo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var edtAtribuicao: EditText
    private lateinit var edtOrcamento: EditText
    private lateinit var btnEnviar: Button

    private var acaoId: Long = -1
    private var pilarSelecionado: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar os campos
        edtTitulo = view.findViewById(R.id.edtTitulo)
        edtDescricao = view.findViewById(R.id.edtDescricao)
        edtAtribuicao = view.findViewById(R.id.edtAtribuicao)
        edtOrcamento = view.findViewById(R.id.edtOrcamento)
        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnEnviar = view.findViewById(R.id.btnEnviar)

        // Recuperar os argumentos
        val args = EditActionArgs.fromBundle(requireArguments())
        acaoId = args.acaoId
        pilarSelecionado = args.pilarSelecionado

        // Carregar os dados da ação
        carregarDadosDaAcao(acaoId)

        // Configurar os DatePickers
        edtStartDate.setOnClickListener {
            mostrarDatePicker(edtStartDate)
        }

        edtEndDate.setOnClickListener {
            mostrarDatePicker(edtEndDate)
        }

        // Configurar o botão Enviar
        btnEnviar.setOnClickListener {
            enviarDados()
        }
    }

    private fun mostrarDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                editText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun carregarDadosDaAcao(acaoId: Long) {
        val acao = ActionRepository(requireContext()).getActionById(acaoId)

        edtTitulo.setText(acao?.titulo)
        edtDescricao.setText(acao?.descricao)
        edtAtribuicao.setText(acao?.responsavel)
        edtOrcamento.setText(acao?.orcamento?.toString() ?: "")
        edtStartDate.setText(acao?.dataInicio)
        edtEndDate.setText(acao?.dataFim)
    }

    private fun enviarDados() {
        // Validação básica
        if (pilarSelecionado.isNullOrEmpty()) {
            // Você poderia mostrar um Toast aqui se quiser
            return
        }

        val titulo = edtTitulo.text.toString()
        val descricao = edtDescricao.text.toString()
        val atribuicao = edtAtribuicao.text.toString()
        val orcamentoTexto = edtOrcamento.text.toString()
        val startDate = edtStartDate.text.toString()
        val endDate = edtEndDate.text.toString()

        if (titulo.isBlank() || descricao.isBlank() || atribuicao.isBlank() || orcamentoTexto.isBlank() || startDate.isBlank() || endDate.isBlank()) {
            // Campos obrigatórios faltando
            return
        }

        val orcamento = orcamentoTexto.toDoubleOrNull()
        if (orcamento == null) {
            // Se o orçamento não for número válido, poderia mostrar erro também
            return
        }

        // Criar a ação atualizada
        val acao = Action(
            id = acaoId,
            titulo = titulo,
            descricao = descricao,
            responsavel = atribuicao,
            orcamento = orcamento,
            dataInicio = startDate,
            dataFim = endDate,
            pilar = pilarSelecionado!!
        )

        // Atualizar a ação no banco de dados
        ActionRepository(requireContext()).updateAction(acao)

        // Voltar para o fragment anterior
        findNavController().navigateUp()
    }
}
