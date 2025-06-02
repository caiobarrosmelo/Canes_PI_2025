package com.example.pi3.coordenador.Actions

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pi3.R
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.data.ActionRepository
import com.example.pi3.data.DBHelper
import com.example.pi3.model.Action
import java.text.SimpleDateFormat
import java.util.*

class EditAction : function_arrow_back() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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

        edtTitulo = view.findViewById(R.id.edtTitulo)
        edtDescricao = view.findViewById(R.id.edtDescricao)
        spinnerResponsavel = view.findViewById(R.id.spinnerResponsavel)
        edtOrcamento = view.findViewById(R.id.edtOrcamento)
        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnEnviar = view.findViewById(R.id.btnEnviar)

        val args = EditActionArgs.fromBundle(requireArguments())
        acaoId = args.acaoId
        pilarSelecionado = args.pilarSelecionado

        configurarSpinnerResponsavel()
        carregarDadosDaAcao(acaoId)

        edtStartDate.setOnClickListener { mostrarDatePicker(edtStartDate) }
        edtEndDate.setOnClickListener { mostrarDatePicker(edtEndDate) }

        btnEnviar.setOnClickListener { enviarDados() }
    }

    private fun configurarSpinnerResponsavel() {
        val dbHelper = DBHelper(requireContext())
        val db = dbHelper.readableDatabase
        val papeisUsuarios = mutableListOf("Selecione o responsável")

        val cursor = db.rawQuery(
            "SELECT DISTINCT papel FROM usuario WHERE papel IN ('apoio', 'coordenador')", null
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
        acao?.let {
            edtTitulo.setText(it.titulo)
            edtDescricao.setText(it.descricao)
            edtOrcamento.setText(it.orcamento.toString())
            edtStartDate.setText(it.dataInicio)
            edtEndDate.setText(it.dataFim)

            val adapter = spinnerResponsavel.adapter as ArrayAdapter<String>
            val pos = adapter.getPosition(it.responsavel)
            if (pos >= 0) spinnerResponsavel.setSelection(pos)
        }
    }

    private fun mostrarDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                editText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun enviarDados() {
        if (pilarSelecionado.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Pilar selecionado inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val titulo = edtTitulo.text.toString()
        val descricao = edtDescricao.text.toString()
        val responsavel = spinnerResponsavel.selectedItem.toString()
        val orcamentoText = edtOrcamento.text.toString()
        val dataInicio = edtStartDate.text.toString()
        val dataFim = edtEndDate.text.toString()
        val orcamento = orcamentoText.toDoubleOrNull() ?: 0.0

        when {
            titulo.isBlank() -> {
                edtTitulo.error = "Título é obrigatório"
                edtTitulo.requestFocus()
            }
            responsavel == "Selecione o responsável" -> {
                Toast.makeText(requireContext(), "Selecione um responsável válido", Toast.LENGTH_SHORT).show()
            }
            orcamento < 0.0 -> {
                edtOrcamento.error = "Orçamento não pode ser negativo"
                edtOrcamento.requestFocus()
            }
            dataInicio.isNotEmpty() && dataFim.isEmpty() -> {
                edtEndDate.error = "Informe a data de fim"
                edtEndDate.requestFocus()
            }
            dataFim.isNotEmpty() && dataInicio.isEmpty() -> {
                edtStartDate.error = "Informe a data de início"
                edtStartDate.requestFocus()
            }
            dataInicio.isNotEmpty() && dataFim.isNotEmpty() -> {
                val datasValidas = try {
                    val inicio = dateFormat.parse(dataInicio)
                    val fim = dateFormat.parse(dataFim)
                    inicio != null && fim != null && fim.after(inicio)
                } catch (e: Exception) {
                    false
                }

                if (!datasValidas) {
                    Toast.makeText(requireContext(), "Data de fim deve ser posterior à de início", Toast.LENGTH_SHORT).show()
                    edtEndDate.requestFocus()
                    return
                }
                atualizarAcao(titulo, descricao, responsavel, orcamento, dataInicio, dataFim)
            }
            else -> {
                // Nenhuma data preenchida
                atualizarAcao(titulo, descricao, responsavel, orcamento, dataInicio, dataFim)
            }
        }
    }

    private fun atualizarAcao(
        titulo: String,
        descricao: String,
        responsavel: String,
        orcamento: Double,
        dataInicio: String,
        dataFim: String
    ) {
        val acao = Action(
            id = acaoId,
            titulo = titulo,
            descricao = descricao,
            responsavel = responsavel,
            orcamento = orcamento,
            dataInicio = dataInicio,
            dataFim = dataFim,
            pilar = pilarSelecionado!!
        )

        ActionRepository(requireContext()).updateAction(acao)

        Toast.makeText(requireContext(), "Ação atualizada com sucesso!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}
