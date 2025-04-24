package com.example.pi3.data

import android.content.ContentValues
import android.content.Context
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie

class ActionRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    // Método para obter ações não aprovadas por pilar
    fun getUnapprovedActionsByPillar(pilar: String): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME} WHERE ${TableActions.COLUMN_PILAR} = ? AND ${TableActions.COLUMN_APROVADA} = 0",
            arrayOf(pilar)
        )
        if (cursor.moveToFirst()) {
            do {
                val action = Action(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(TableActions.COLUMN_ID)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_TITULO)),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DESCRICAO)),
                    responsavel = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_RESPONSAVEL)),
                    orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow(TableActions.COLUMN_ORCAMENTO)),
                    dataInicio = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DATA_INICIO)),
                    dataFim = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DATA_FIM)),
                    pilar = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_PILAR)),
                    aprovada = cursor.getInt(cursor.getColumnIndexOrThrow(TableActions.COLUMN_APROVADA)) == 1
                )
                lista.add(action)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun getApprovedActionsByPillar(pilar: String): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME} WHERE ${TableActions.COLUMN_PILAR} = ? AND ${TableActions.COLUMN_APROVADA} = 1",
            arrayOf(pilar)
        )
        if (cursor.moveToFirst()) {
            do {
                val action = Action(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(TableActions.COLUMN_ID)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_TITULO)),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DESCRICAO)),
                    responsavel = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_RESPONSAVEL)),
                    orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow(TableActions.COLUMN_ORCAMENTO)),
                    dataInicio = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DATA_INICIO)),
                    dataFim = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_DATA_FIM)),
                    pilar = cursor.getString(cursor.getColumnIndexOrThrow(TableActions.COLUMN_PILAR)),
                    aprovada = true
                )
                lista.add(action)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }


    fun aprovarAcao(id: Long): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActions.COLUMN_APROVADA, 1)
        }
        val rowsUpdated = db.update(
            TableActions.TABLE_NAME,
            values,
            "${TableActions.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun recusarAcao(id: Long): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TableActions.COLUMN_APROVADA, -1)
        }
        val rowsUpdated = db.update(
            TableActions.TABLE_NAME,
            values,
            "${TableActions.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsUpdated > 0
    }

    fun getActivitiesByActionId(acaoId: Long): List<Activitie> {
        val atividades = mutableListOf<Activitie>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActivities.TABLE_NAME} WHERE ${TableActivities.COLUMN_ACAO_ID} = ?",
            arrayOf(acaoId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val atividade = Activitie(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                    responsavel = cursor.getString(cursor.getColumnIndexOrThrow("responsavel")),
                    orcamento = cursor.getDouble(cursor.getColumnIndexOrThrow("orcamento")),
                    dataInicio = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio")),
                    dataFim = cursor.getString(cursor.getColumnIndexOrThrow("data_fim")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1,
                    aprovada = cursor.getInt(cursor.getColumnIndexOrThrow("aprovada")) == 1,
                    acaoId = cursor.getLong(cursor.getColumnIndexOrThrow("acaoId"))
                )
                atividades.add(atividade)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return atividades
    }

    fun insertAction(action: Action) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", action.titulo)
            put("descricao", action.descricao)
            put("responsavel", action.responsavel)
            put("orcamento", action.orcamento)
            put("data_inicio", action.dataInicio)
            put("data_fim", action.dataFim)
            put("pilar", action.pilar)
            put("aprovada", if (action.aprovada) 1 else 0)
        }
        db.insert("actions", null, values)
    }
}



