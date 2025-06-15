package com.example.pi3.coordenador.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pi3.R
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.coordenador.activities.ActivitieRegisterArgs
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.data.DBHelper
import com.example.pi3.model.Activitie
import java.text.SimpleDateFormat
import java.util.*

//lógica do formulário de registro de atividades do coordenador
class ActivitieRegister : function_arrow_back() {

    private lateinit var edtTitulo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var spinnerResponsavel: Spinner
    private lateinit var edtOrcamento: EditText
    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var btnEnviar: Button

    private var acaoId: Long = -1
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activitie_register, container, false)
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

        // Obter ID da ação associada
        val args = ActivitieRegisterArgs.fromBundle(requireArguments())
        acaoId = args.acaoId

        // Preencher spinner
        val dbHelper = DBHelper(requireContext())
        val db = dbHelper.readableDatabase
        val nomesUsuarios = mutableListOf("Selecione o responsável")

        val cursor = db.rawQuery("SELECT papel FROM usuario WHERE papel IN ('apoio', 'coordenador')", null)
        if (cursor.moveToFirst()) {
            do {
                nomesUsuarios.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nomesUsuarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerResponsavel.adapter = adapter

        // DatePickers
        edtStartDate.setOnClickListener { showDatePickerDialog(edtStartDate) }
        edtEndDate.setOnClickListener { showDatePickerDialog(edtEndDate) }

        // Botão Enviar
        btnEnviar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()
            val responsavel = spinnerResponsavel.selectedItem.toString()
            val orcamento = edtOrcamento.text.toString().toDoubleOrNull() ?: 0.0
            val dataInicio = edtStartDate.text.toString()
            val dataFim = edtEndDate.text.toString()

            // Validações
            when {
                titulo.isEmpty() -> {
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
                (dataInicio.isNotEmpty() && dataFim.isEmpty()) -> {
                    edtEndDate.error = "Informe a data de fim"
                    edtEndDate.requestFocus()
                }
                (dataFim.isNotEmpty() && dataInicio.isEmpty()) -> {
                    edtStartDate.error = "Informe a data de início"
                    edtStartDate.requestFocus()
                }
                (dataInicio.isNotEmpty() && dataFim.isNotEmpty()) -> {
                    val datasValidas = try {
                        val inicioDate = dateFormat.parse(dataInicio)
                        val fimDate = dateFormat.parse(dataFim)
                        inicioDate != null && fimDate != null && fimDate.after(inicioDate)
                    } catch (e: Exception) {
                        false
                    }
                    if (!datasValidas) {
                        Toast.makeText(requireContext(), "Data de fim deve ser posterior à de início", Toast.LENGTH_SHORT).show()
                        edtEndDate.requestFocus()
                    } else {
                        salvarAtividade(titulo, descricao, responsavel, orcamento, dataInicio, dataFim)
                    }
                }
                // Nenhuma data preenchida — OK
                else -> {
                    salvarAtividade(titulo, descricao, responsavel, orcamento, dataInicio, dataFim)
                }
            }
        }
    }

    // função para salvar a atividade no banco de dados
    private fun salvarAtividade(
        titulo: String,
        descricao: String,
        responsavel: String,
        orcamento: Double,
        dataInicio: String,
        dataFim: String
    ) {
        val activitie = Activitie(
            titulo = titulo,
            descricao = descricao,
            responsavel = responsavel,
            orcamento = orcamento,
            dataInicio = dataInicio,
            dataFim = dataFim,
            aprovada = false,
            acaoId = acaoId
        )

        val repository = ActivitieRepository(requireContext())
        repository.insertActivitie(activitie)

        Toast.makeText(requireContext(), "Atividade registrada!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    //calendário para definir as datas de início e fim da atividade
    private fun showDatePickerDialog(targetEditText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                targetEditText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
