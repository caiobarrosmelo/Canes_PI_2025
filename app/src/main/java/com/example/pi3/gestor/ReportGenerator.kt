package com.example.pi3.gestor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.pi3.data.ActionRepository
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportGenerator(private val context: Context) {
    private val actionRepository = ActionRepository(context)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun generateReport(pilarSelecionado: String): Uri? {
        try {
            // Criar arquivo temporário para o PDF
            val fileName = "relatorio_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)

            // Inicializar o PDF
            val pdfWriter = PdfWriter(outputStream)
            val pdf = PdfDocument(pdfWriter)
            val document = Document(pdf)

            // Adicionar título
            val title = Paragraph("Relatório de Ações")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
            document.add(title)

            // Adicionar data de geração
            val dataGeracao = Paragraph("Data de geração: ${dateFormat.format(Date())}")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(12f)
            document.add(dataGeracao)

            // Adicionar pilar selecionado
            val pilar = Paragraph("Pilar: $pilarSelecionado")
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(14f)
            document.add(pilar)

            // Obter ações
            val acoes = if (pilarSelecionado == "Todos") {
                actionRepository.getAllActions()
            } else {
                actionRepository.getActionsByPilar(pilarSelecionado)
            }.filter { it.aprovada }

            // Criar tabela de ações
            val table = Table(UnitValue.createPercentArray(5)).useAllAvailableWidth()
            
            // Adicionar cabeçalho
            val headers = arrayOf("Título", "Responsável", "Data Início", "Data Fim", "Status")
            headers.forEach { header ->
                table.addHeaderCell(Cell().add(Paragraph(header)).setBold())
            }

            // Adicionar dados das ações
            acoes.forEach { acao ->
                table.addCell(Cell().add(Paragraph(acao.titulo)))
                table.addCell(Cell().add(Paragraph(acao.responsavel)))
                table.addCell(Cell().add(Paragraph(acao.dataInicio)))
                table.addCell(Cell().add(Paragraph(acao.dataFim)))
                table.addCell(Cell().add(Paragraph(getStatusAcao(acao))))
            }

            document.add(table)

            // Adicionar estatísticas
            val acoesConcluidas = acoes.count { isActionConcluida(it) }
            val acoesEmAndamento = acoes.count { isActionEmAndamento(it) }
            val acoesAtrasadas = acoes.count { isActionAtrasada(it) }

            val estatisticas = Paragraph("\nEstatísticas:")
                .setFontSize(14f)
                .setBold()
            document.add(estatisticas)

            val estatisticasDetalhes = """
                Total de ações: ${acoes.size}
                Ações concluídas: $acoesConcluidas
                Ações em andamento: $acoesEmAndamento
                Ações atrasadas: $acoesAtrasadas
            """.trimIndent()

            document.add(Paragraph(estatisticasDetalhes))

            // Fechar documento
            document.close()

            // Retornar URI do arquivo
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getStatusAcao(acao: Action): String {
        return when {
            isActionConcluida(acao) -> "Concluída"
            isActionAtrasada(acao) -> "Atrasada"
            isActionEmAndamento(acao) -> "Em andamento"
            else -> "Não iniciada"
        }
    }

    private fun isActionConcluida(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        return atividades.isNotEmpty() && atividades.all { it.status == Activitie.STATUS_CONCLUIDA }
    }

    private fun isActionEmAndamento(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val temAtividadeEmAndamento = atividades.any { it.status == Activitie.STATUS_EM_ANDAMENTO && dateFormat.parse(it.dataFim)?.after(hoje) == true }
        val temAtividadeAtrasada = atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }

        return atividades.isNotEmpty() && temAtividadeEmAndamento && !temAtividadeAtrasada && !isActionConcluida(action)
    }

    private fun isActionAtrasada(action: Action): Boolean {
        val atividades = actionRepository.getActivitiesByActionId(action.id)
        val hoje = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return atividades.any { it.status != Activitie.STATUS_CONCLUIDA && dateFormat.parse(it.dataFim)?.before(hoje) == true }
    }
} 