package com.example.pi3.apoio.actions

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.pi3.R
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.coordenador.Actions.EditActionArgs
import com.example.pi3.data.ActionRepository
import com.example.pi3.data.DBHelper
import com.example.pi3.model.Action
import java.util.Calendar

class EditAction : function_arrow_back() {

    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var edtTitulo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var spinnerResponsavel: Spinner
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
        spinnerResponsavel = view.findViewById(R.id.spinnerResponsavel)
        edtOrcamento = view.findViewById(R.id.edtOrcamento)
        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnEnviar = view.findViewById(R.id.btnEnviar)

        // Recuperar os argumentos
        val args = EditActionArgs.fromBundle(requireArguments())
        acaoId = args.acaoId
        pilarSelecionado = args.pilarSelecionado

        // Configurar o spinner de responsáveis (lista de papéis)
        configurarSpinnerResponsavel()

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

    private fun configurarSpinnerResponsavel() {
        val dbHelper = DBHelper(requireContext())
        val db = dbHelper.readableDatabase
        val papeisUsuarios = mutableListOf("Selecione o responsável")

        val cursor = db.rawQuery(
            "SELECT DISTINCT papel FROM usuario WHERE papel IN ('apoio', 'coordenador')",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                papeisUsuarios.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, papeisUsuarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerResponsavel.adapter = adapter
    }

    private fun carregarDadosDaAcao(acaoId: Long) {
        val acao = ActionRepository(requireContext()).getActionById(acaoId)
        if (acao != null) {
            edtTitulo.setText(acao.titulo)
            edtDescricao.setText(acao.descricao)
            edtOrcamento.setText(acao.orcamento.toString())
            edtStartDate.setText(acao.dataInicio)
            edtEndDate.setText(acao.dataFim)

            // Ajustar seleção do spinner para o responsável atual (papel)
            val adapter = spinnerResponsavel.adapter as ArrayAdapter<String>
            val pos = adapter.getPosition(acao.responsavel)
            if (pos >= 0) {
                spinnerResponsavel.setSelection(pos)
            }
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

    private fun enviarDados() {
        // Validação básica
        if (pilarSelecionado.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Pilar selecionado inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val titulo = edtTitulo.text.toString()
        val descricao = edtDescricao.text.toString()
        val responsavel = spinnerResponsavel.selectedItem.toString()
        val orcamentoTexto = edtOrcamento.text.toString()
        val startDate = edtStartDate.text.toString()
        val endDate = edtEndDate.text.toString()

        if (titulo.isBlank() || descricao.isBlank() || responsavel == "Selecione o responsável" ||
            orcamentoTexto.isBlank() || startDate.isBlank() || endDate.isBlank()
        ) {
            Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            return
        }

        val orcamento = orcamentoTexto.toDoubleOrNull()
        if (orcamento == null) {
            Toast.makeText(requireContext(), "Orçamento inválido", Toast.LENGTH_SHORT).show()
            return
        }

        // Criar a ação atualizada
        val acao = Action(
            id = acaoId,
            titulo = titulo,
            descricao = descricao,
            responsavel = responsavel,
            orcamento = orcamento,
            dataInicio = startDate,
            dataFim = endDate,
            pilar = pilarSelecionado!!
        )

        // Atualizar a ação no banco de dados
        ActionRepository(requireContext()).updateAction(acao)

        Toast.makeText(requireContext(), "Ação atualizada com sucesso!", Toast.LENGTH_SHORT).show()

        // Voltar para o fragment anterior
        findNavController().navigateUp()
    }
}