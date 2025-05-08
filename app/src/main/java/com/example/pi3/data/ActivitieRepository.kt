package com.example.pi3.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.pi3.model.Activitie

class ActivitieRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    fun insertActivitie(activitie: Activitie) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_TITULO, activitie.titulo)
            put(TableActivities.COLUMN_DESCRICAO, activitie.descricao)
            put(TableActivities.COLUMN_RESPONSAVEL, activitie.responsavel)
            put(TableActivities.COLUMN_ORCAMENTO, activitie.orcamento)
            put(TableActivities.COLUMN_DATA_INICIO, activitie.dataInicio)
            put(TableActivities.COLUMN_DATA_FIM, activitie.dataFim)
            put(TableActivities.COLUMN_STATUS, if (activitie.status) 1 else 0)
            put(TableActivities.COLUMN_APROVADA, if (activitie.aprovada) 1 else 0)
            put(TableActivities.COLUMN_ACAO_ID, activitie.acaoId)
        }
        val result = db.insert(TableActivities.TABLE_NAME, null, values)
        Log.d("ActivitieRepository", "Inserção na tabela ${TableActivities.TABLE_NAME}: result=$result")
    }


    fun getUnapprovedActivities(): List<Activitie> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TableActivities.TABLE_NAME,
            null,
            "${TableActivities.COLUMN_APROVADA} = 0",
            null,
            null,
            null,
            null
        )

        val lista = mutableListOf<Activitie>()
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Activitie(
                        id = it.getLong(it.getColumnIndexOrThrow(TableActivities.COLUMN_ID)),
                        titulo = it.getString(it.getColumnIndexOrThrow(TableActivities.COLUMN_TITULO)),
                        descricao = it.getString(it.getColumnIndexOrThrow(TableActivities.COLUMN_DESCRICAO)),
                        responsavel = it.getString(it.getColumnIndexOrThrow(TableActivities.COLUMN_RESPONSAVEL)),
                        orcamento = it.getDouble(it.getColumnIndexOrThrow(TableActivities.COLUMN_ORCAMENTO)),
                        dataInicio = it.getString(it.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_INICIO)),
                        dataFim = it.getString(it.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_FIM)),
                        status = it.getInt(it.getColumnIndexOrThrow(TableActivities.COLUMN_STATUS)) == 1,
                        aprovada = false,
                        acaoId = it.getLong(it.getColumnIndexOrThrow(TableActivities.COLUMN_ACAO_ID))
                    )
                )
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
            "${TableActivities.COLUMN_ACAO_ID} = ? AND ${TableActivities.COLUMN_APROVADA} = ?", // Alterado para filtrar por aprovada
            arrayOf(acaoId.toString(), "1"),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            activities.add(
                Activitie(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ID)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_TITULO)),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DESCRICAO)),
                    responsavel = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_RESPONSAVEL)),
                    orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ORCAMENTO)),
                    dataInicio = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_INICIO)),
                    dataFim = cursor.getString(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_DATA_FIM)),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_STATUS)) == 1,
                    aprovada = cursor.getInt(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_APROVADA)) == 1,
                    acaoId = cursor.getLong(cursor.getColumnIndexOrThrow(TableActivities.COLUMN_ACAO_ID))
                )
            )
        }
        cursor.close()
        return activities
    }

    fun atualizarStatusAtividade(id: Long, status: Boolean): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_STATUS, if (status) 1 else 0)
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


    fun aprovarAtividade(id: Long): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_APROVADA, 1)
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


    fun desaprovarAtividade(id: Long): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActivities.COLUMN_APROVADA, 0)
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



}