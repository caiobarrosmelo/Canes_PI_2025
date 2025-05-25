package com.example.pi3.coordenador.Actions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pi3.R
import com.example.pi3.data.DBHelper
import android.app.DatePickerDialog

import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action

import java.text.SimpleDateFormat
import java.util.*

class ActionRegisterFragment : function_arrow_back() {

    private lateinit var edtTitulo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var spinnerResponsavel: Spinner
    private lateinit var edtOrcamento: EditText
    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var btnEnviar: Button

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var pilarSelecionado: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_action_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar os componentes
        edtTitulo = view.findViewById(R.id.edtTitulo)
        edtDescricao = view.findViewById(R.id.edtDescricao)
        spinnerResponsavel = view.findViewById(R.id.spinnerResponsavel)
        edtOrcamento = view.findViewById(R.id.edtOrcamento)
        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnEnviar = view.findViewById(R.id.btnEnviar)

        // Recuperar o pilar selecionado do Safe Args
        pilarSelecionado = arguments?.getString("pilarSelecionado")

        // Preencher spinner com nomes dos usuários
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

        // Ação do botão Enviar
        btnEnviar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()
            val responsavel = spinnerResponsavel.selectedItem.toString()
            val orcamento = edtOrcamento.text.toString().toDoubleOrNull() ?: 0.0
            val dataInicio = edtStartDate.text.toString()
            val dataFim = edtEndDate.text.toString()

            val camposPreenchidos = titulo.isNotEmpty() &&
                    descricao.isNotEmpty() &&
                    responsavel != "Selecione o responsável" &&
                    orcamento > 0.0 &&
                    dataInicio.isNotEmpty() &&
                    dataFim.isNotEmpty() &&
                    pilarSelecionado != null

            if (camposPreenchidos) {
                val action = Action(
                    titulo = titulo,
                    descricao = descricao,
                    responsavel = responsavel,
                    orcamento = orcamento,
                    dataInicio = dataInicio,
                    dataFim = dataFim,
                    pilar = pilarSelecionado!!,
                    aprovada = false
                )

                val repository = ActionRepository(requireContext())
                repository.insertAction(action)

                Toast.makeText(requireContext(), "Ação registrada!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

