package com.example.pi3.apoio.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pi3.R
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
    private lateinit var edtResponsavel: EditText
    private lateinit var edtOrcamento: EditText
    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var btnEnviar: Button

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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
        edtResponsavel = view.findViewById(R.id.edtAtribuicao)  // Responsável
        edtOrcamento = view.findViewById(R.id.edtOrcamento)
        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnEnviar = view.findViewById(R.id.btnEnviar)

        // Recuperar o pilar selecionado do Safe Args
        pilarSelecionado = arguments?.getString("pilarSelecionado")

        // Abertura do DatePicker ao tocar nos campos
        edtStartDate.setOnClickListener { showDatePickerDialog(edtStartDate) }
        edtEndDate.setOnClickListener { showDatePickerDialog(edtEndDate) }

        // Enviar ação
        btnEnviar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()
            val responsavel = edtResponsavel.text.toString()  // Responsável
            val orcamento = edtOrcamento.text.toString().toDoubleOrNull() ?: 0.0
            val dataInicio = edtStartDate.text.toString()
            val dataFim = edtEndDate.text.toString()

            // Verifique se todos os campos foram preenchidos corretamente
            if (titulo.isNotEmpty() && descricao.isNotEmpty() && responsavel.isNotEmpty() &&
                orcamento > 0.0 && dataInicio.isNotEmpty() && dataFim.isNotEmpty() && pilarSelecionado != null) {

                // Criar objeto de Ação
                val action = Action(
                    titulo = titulo,
                    descricao = descricao,
                    responsavel = responsavel,
                    orcamento = orcamento,
                    dataInicio = dataInicio,
                    dataFim = dataFim,
                    pilar = pilarSelecionado!!,
                    aprovada = false  // A ação começa como não aprovada
                )

                // Salvar a ação no banco de dados
                val repository = ActionRepository(requireContext())
                repository.insertAction(action)

                // Exibir mensagem de sucesso
                Toast.makeText(requireContext(), "Ação registrada!", Toast.LENGTH_SHORT).show()

                // Voltar para a tela anterior
                findNavController().popBackStack()
            } else {
                // Exibir mensagem de erro se os campos não estiverem preenchidos
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