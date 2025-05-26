package com.example.pi3.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.pi3.model.Action
import com.example.pi3.model.Activitie

class ActionRepository(context: Context) {

    private val dbHelper = DBHelper(context)

    // Método para obter todas as ações
    fun getAllActions(): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME}",
            null
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

    // Método para obter ações por pilar
    fun getActionsByPilar(pilar: String): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME} WHERE ${TableActions.COLUMN_PILAR} = ?",
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

    // Método para obter ações não aprovadas por pilar
    fun getUnapprovedActionsByPillar(pilar: String): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME} WHERE ${TableActions.COLUMN_APROVADA} = 0",
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


    fun getUnapprovedActions(): List<Action> {
        val lista = mutableListOf<Action>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${TableActions.TABLE_NAME} WHERE ${TableActions.COLUMN_APROVADA} = 0",
            null
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
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("status")),
                    aprovada = cursor.getInt(cursor.getColumnIndexOrThrow("aprovada")) == 1,
                    acaoId = cursor.getLong(cursor.getColumnIndexOrThrow("acao_id"))
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

    fun updateAction(action: Action) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(TableActions.COLUMN_TITULO, action.titulo)
            put(TableActions.COLUMN_DESCRICAO, action.descricao)
            put(TableActions.COLUMN_RESPONSAVEL, action.responsavel)
            put(TableActions.COLUMN_ORCAMENTO, action.orcamento)
            put(TableActions.COLUMN_DATA_INICIO, action.dataInicio)
            put(TableActions.COLUMN_DATA_FIM, action.dataFim)
        }

        // Usando db.update para atualizar a ação
        val rowsUpdated = db.update(
            TableActions.TABLE_NAME,   // Nome da tabela
            contentValues,             // Valores a serem atualizados
            "${TableActions.COLUMN_ID} = ?", // Condição para identificar a linha
            arrayOf(action.id.toString())  // Parâmetros da condição
        )

        if (rowsUpdated > 0) {
            // Sucesso na atualização
            Log.d("UpdateAction", "Ação atualizada com sucesso!")
        } else {
            // Se nenhuma linha foi atualizada, significa que não havia uma ação com o ID fornecido
            Log.d("UpdateAction", "Nenhuma ação foi atualizada. Verifique o ID.")
        }

        // Fechar o banco de dados após a operação
        db.close()
    }

    fun getActionById(id: Long): Action? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            TableActions.TABLE_NAME,
            null, // Todas as colunas
            "${TableActions.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use { // Fecha o cursor automaticamente depois do bloco
            if (it.moveToFirst()) {
                return Action(
                    id = it.getLong(it.getColumnIndexOrThrow(TableActions.COLUMN_ID)),
                    titulo = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_TITULO)),
                    descricao = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_DESCRICAO)),
                    responsavel = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_RESPONSAVEL)),
                    orcamento = it.getDouble(it.getColumnIndexOrThrow(TableActions.COLUMN_ORCAMENTO)),
                    dataInicio = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_DATA_INICIO)),
                    dataFim = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_DATA_FIM)),
                    pilar = it.getString(it.getColumnIndexOrThrow(TableActions.COLUMN_PILAR)),
                    aprovada = it.getInt(it.getColumnIndexOrThrow(TableActions.COLUMN_APROVADA)) == 1
                )
            }
        }

        return null
    }




}



