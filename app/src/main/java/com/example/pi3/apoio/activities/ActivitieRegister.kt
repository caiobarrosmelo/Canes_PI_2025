package com.example.pi3.apoio.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pi3.R
import com.example.pi3.adapters.function_arrow_back
import com.example.pi3.data.ActivitieRepository
import com.example.pi3.model.Activitie
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ActivitieRegister : function_arrow_back() {

    private lateinit var edtTitulo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var edtResponsavel: EditText
    private lateinit var edtOrcamento: EditText
    private lateinit var edtStartDate: EditText
    private lateinit var edtEndDate: EditText
    private lateinit var btnEnviar: Button

    private var acaoId: Long = -1

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activitie_register, container, false)
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

        val args = ActivitieRegisterArgs.fromBundle(requireArguments())
        acaoId = args.acaoId

        // Abertura do DatePicker ao tocar nos campos
        edtStartDate.setOnClickListener { showDatePickerDialog(edtStartDate) }
        edtEndDate.setOnClickListener { showDatePickerDialog(edtEndDate) }

        // Enviar ação
        btnEnviar.setOnClickListener {
            val titulo = edtTitulo.text.toString()
            val descricao = edtDescricao.text.toString()
            val responsavel = edtResponsavel.text.toString()
            val orcamento = edtOrcamento.text.toString().toDoubleOrNull() ?: 0.0
            val dataInicio = edtStartDate.text.toString()
            val dataFim = edtEndDate.text.toString()

            // Verifique se todos os campos foram preenchidos corretamente
            if (titulo.isNotEmpty() && descricao.isNotEmpty() && responsavel.isNotEmpty() &&
                orcamento > 0.0 && dataInicio.isNotEmpty() && dataFim.isNotEmpty()) {

                // Criar objeto de Ação
                val activitie = Activitie(
                    titulo = titulo,
                    descricao = descricao,
                    responsavel = responsavel,
                    orcamento = orcamento,
                    dataInicio = dataInicio,
                    dataFim = dataFim,
                    aprovada = false,  // A ação começa como não aprovada
                    acaoId = acaoId
                )

                // Salvar a ação no banco de dados
                val repository = ActivitieRepository(requireContext())
                repository.insertActivitie(activitie)

                // Exibir mensagem de sucesso
                Toast.makeText(requireContext(), "Atividade registrada, aguardando aprovação.", Toast.LENGTH_SHORT).show()

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