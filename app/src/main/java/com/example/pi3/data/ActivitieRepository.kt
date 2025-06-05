package com.example.pi3.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.pi3.model.Activitie
import com.example.pi3.data.TableActivities // Importar TableActivities
import java.text.SimpleDateFormat
import java.util.*

// Constantes para os status das atividades
object StatusAtividade {

    const val PENDENTE_APROVACAO_INICIAL = 0
    const val EM_PROCESSO = 1
    const val PENDENTE_APROVACAO_CONCLUSAO = 2
    const val CONCLUIDA = 3
    const val REJEITADA_CONCLUSAO = 4
    const val REJEITADA_INICIAL = 5
}

class ActivitieRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertActivitie(activitie: Activitie): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_TITULO, activitie.titulo)
            put(TableActivities.COLUMN_DESCRICAO, activitie.descricao)
            put(TableActivities.COLUMN_RESPONSAVEL, activitie.responsavel)
            put(TableActivities.COLUMN_ORCAMENTO, activitie.orcamento)
            put(TableActivities.COLUMN_DATA_INICIO, activitie.dataInicio)
            put(TableActivities.COLUMN_DATA_FIM, activitie.dataFim)
            put(TableActivities.COLUMN_STATUS, activitie.status)
            put(TableActivities.COLUMN_APROVADA, if (activitie.aprovada) 1 else 0)
            put(TableActivities.COLUMN_ACAO_ID, activitie.acaoId)
        }
        val newRowId = db.insert(TableActivities.TABLE_NAME, null, values)
        db.close()
        Log.d("ActivitieRepository", "Inserção na tabela ${TableActivities.TABLE_NAME}: result=$newRowId")
        return newRowId
    }

    private fun cursorToActivitie(cursor: android.database.Cursor): Activitie {
         return Activitie(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ID)),
            titulo = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_TITULO)),
            descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DESCRICAO)),
            responsavel = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_RESPONSAVEL)),
            orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ORCAMENTO)),
            dataInicio = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_INICIO)),
            dataFim = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_FIM)),
            status = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_STATUS)),
            aprovada = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_APROVADA)) == 1,
            acaoId = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ACAO_ID))
        )
    }

    fun getActivitiesPendingApproval(): List<Activitie> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_APROVADA} = ? AND ${TableActivities.COLUMN_STATUS} = ?",
            arrayOf("0", StatusAtividade.PENDENTE_APROVACAO_INICIAL.toString()),
            null,
            null,
            null
        )

        val lista = mutableListOf<Activitie>()
        cursor.use {
            while (it.moveToNext()) {
                lista.add(cursorToActivitie(it))
            }
        }
        return lista
    }

    fun getActivitiesPendingCompletionApproval(): List<Activitie> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_APROVADA} = ? AND ${TableActivities.COLUMN_STATUS} = ?",
            arrayOf("1", StatusAtividade.PENDENTE_APROVACAO_CONCLUSAO.toString()),
            null,
            null,
            null
        )

        val lista = mutableListOf<Activitie>()
        cursor.use {
            while (it.moveToNext()) {
                lista.add(cursorToActivitie(it))
            }
        }
        return lista
    }

    fun getActivitiesApprovedByActionId(acaoId: Long): List<Activitie> {
        val activities = mutableListOf<Activitie>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_ACAO_ID} = ? AND ${TableActivities.COLUMN_APROVADA} = ?",
            arrayOf(acaoId.toString(), "1"),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            activities.add(cursorToActivitie(cursor))
        }
        cursor.close()
        return activities
    }

    fun getAllApprovedActivities(): List<Activitie> {
        val activities = mutableListOf<Activitie>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_APROVADA} = ?",
            arrayOf("1"),
            null,
            null,
            "${TableActivities.COLUMN_DATA_FIM} DESC"
        )
        while (cursor.moveToNext()) {
            activities.add(cursorToActivitie(cursor))
        }
        cursor.close()
        return activities
    }

    fun updateActivitieStatus(id: Long, status: Int): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_STATUS, status)
        }
        val rowsUpdated = db.update(
            TableActivities.TABLE_NAME,
            values,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

     fun updateActivitieAprovada(id: Long, aprovada: Boolean): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_APROVADA, if (aprovada) 1 else 0)
        }
        val rowsUpdated = db.update(
            TableActivities.TABLE_NAME,
            values,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun aprovarAtividadeInicial(id: Long): Boolean {
        // Marca como aprovada (aprovada = 1) e define status para EM_PROCESSO
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_APROVADA, 1)
            put(TableActivities.COLUMN_STATUS, StatusAtividade.EM_PROCESSO)
        }
        val rowsUpdated = db.update(
            TableActivities.TABLE_NAME,
            values,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }


    fun desaprovarAtividadeInicial(id: Long): Boolean {
        // Marca como não aprovada (aprovada = 0) e define status para REJEITADA_INICIAL
         val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_APROVADA, 0)
            put(TableActivities.COLUMN_STATUS, StatusAtividade.REJEITADA_INICIAL)
        }
        val rowsUpdated = db.update(
            TableActivities.TABLE_NAME,
            values,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun marcarComoConcluidaPendenteAprovacao(id: Long): Boolean {
        // Marca status como PENDENTE_APROVACAO_CONCLUSAO (aprovada já deve ser 1)
        return updateActivitieStatus(id, StatusAtividade.PENDENTE_APROVACAO_CONCLUSAO)
    }

    fun aprovarConclusaoAtividade(id: Long): Boolean {
        // Marca status como CONCLUIDA (aprovada já deve ser 1)
        return updateActivitieStatus(id, StatusAtividade.CONCLUIDA)
    }

    fun rejeitarConclusaoAtividade(id: Long): Boolean {
        // Marca status como REJEITADA_CONCLUSAO, voltando para EM_PROCESSO (aprovada já deve ser 1)
        return updateActivitieStatus(id, StatusAtividade.EM_PROCESSO)
    }

    fun getActivityById(id: Long): Activitie? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return try {
            if (cursor.moveToFirst()) {
                cursorToActivitie(cursor)
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    fun deleteActivityById(id: Long): Boolean {
        val db = dbHelper.writableDatabase
        val rowsAffected = db.delete(
            TableActivities.TABLE_NAME,
            "${TableActivities.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsAffected > 0
    }

    fun getApprovedActivitiesByResponsavel(responsavel: String): List<Activitie> {
        val activities = mutableListOf<Activitie>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_APROVADA} = ? AND LOWER(${TableActivities.COLUMN_RESPONSAVEL}) = ?",
            arrayOf("1", responsavel.lowercase()),
            null,
            null,
            "${TableActivities.COLUMN_DATA_FIM} DESC"
        )
        while (cursor.moveToNext()) {
            activities.add(cursorToActivitie(cursor))
        }
        cursor.close()
        return activities
    }

    fun getExpiringActivitiesByActionId(acaoId: Long): List<Activitie> {
        val activities = mutableListOf<Activitie>()
        val db = dbHelper.readableDatabase
        val currentDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateStr = dateFormat.format(currentDate.time)

        Log.d("ActivitieRepository", "Buscando atividades para acaoId=$acaoId, in-progress ou expiradas antes de $currentDateStr")

        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_ACAO_ID} = ? AND (${TableActivities.COLUMN_STATUS} = ? OR ${TableActivities.COLUMN_DATA_FIM} < ?)",
            arrayOf(acaoId.toString(), Activitie.STATUS_EM_ANDAMENTO.toString(), currentDateStr),
            null,
            null,
            "${TableActivities.COLUMN_DATA_FIM} ASC"
        )

        while (cursor.moveToNext()) {
            val activity = Activitie(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ID)),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_TITULO)),
                descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DESCRICAO)),
                responsavel = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_RESPONSAVEL)),
                orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ORCAMENTO)),
                dataInicio = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_INICIO)),
                dataFim = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_FIM)),
                status = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_STATUS)),
                aprovada = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_APROVADA)) == 1,
                acaoId = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ACAO_ID))
            )
            activities.add(activity)
            Log.d("ActivitieRepository", "Activity: ID=${activity.id}, Title=${activity.titulo}, Status=${activity.status}, DataFim=${activity.dataFim}")
        }
        cursor.close()
        return activities
    }

    fun calculateDaysRemaining(dataFim: String?): Long {
        if (dataFim.isNullOrEmpty()) return 0
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val endDate = dateFormat.parse(dataFim) ?: return 0
            val currentDate = Calendar.getInstance().time
            val diffMillis = endDate.time - currentDate.time
            return (diffMillis / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
        } catch (e: Exception) {
            Log.e("ActivitieRepository", "Erro ao calcular dias restantes: $e")
            return 0
        }
    }

    }




