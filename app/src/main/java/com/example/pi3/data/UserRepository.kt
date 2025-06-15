package com.example.pi3.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.pi3.model.UserRole

//classe que define os métodos que manipulam a tabela usuário
class UserRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    fun loginUsers(papel: String, senha: String): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${TableUsers.COLUMN_PAPEL} FROM ${TableUsers.TABLE_NAME} WHERE ${TableUsers.COLUMN_PAPEL} = ? AND ${TableUsers.COLUMN_SENHA} = ?",
            arrayOf(papel, senha)
        )

        var papelEncontrado: String? = null
        if (cursor.moveToFirst()) {
            papelEncontrado = cursor.getString(0) // Retorna "apoio", "coordenador" ou "gestor"
        }
        cursor.close()
        return papelEncontrado
    }

    fun insertUsers(role: UserRole): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(TableUsers.COLUMN_PAPEL, role.name.lowercase())
            put(TableUsers.COLUMN_SENHA, role.senhaPadrao)
        }
        return db.insert(TableUsers.TABLE_NAME, null, cv)
    }

    fun listUsers(): List<UserRole> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            TableUsers.TABLE_NAME,
            arrayOf(TableUsers.COLUMN_PAPEL),
            null,
            null,
            null,
            null,
            null
        )

        val list = mutableListOf<UserRole>()
        while (cursor.moveToNext()) {
            val papelStr = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_PAPEL))
            try {
                val role = UserRole.fromString(papelStr)
                list.add(role)
            } catch (e: IllegalArgumentException) {
                // Ignora papéis inválidos no banco
            }
        }
        cursor.close()
        return list
    }

    fun deleteAllUsers(): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TableUsers.TABLE_NAME, null, null)
    }
}

