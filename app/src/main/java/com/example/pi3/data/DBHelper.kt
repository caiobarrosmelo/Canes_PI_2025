package com.example.pi3.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(
    context, "seuprojeto.db", null, 2
) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_ACTIONS_TABLE = """
            CREATE TABLE ${TableActions.TABLE_NAME} (
                ${TableActions.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TableActions.COLUMN_TITULO} TEXT,
                ${TableActions.COLUMN_DESCRICAO} TEXT,
                ${TableActions.COLUMN_RESPONSAVEL} TEXT,
                ${TableActions.COLUMN_ORCAMENTO} REAL,
                ${TableActions.COLUMN_DATA_INICIO} TEXT,
                ${TableActions.COLUMN_DATA_FIM} TEXT,
                ${TableActions.COLUMN_PILAR} TEXT,
                ${TableActions.COLUMN_APROVADA} INTEGER DEFAULT 0
            )
        """
        db.execSQL(CREATE_ACTIONS_TABLE)

        val CREATE_ACTIVITIES_TABLE = """
            CREATE TABLE ${TableActivities.TABLE_NAME} (
                ${TableActivities.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TableActivities.COLUMN_TITULO} TEXT,
                ${TableActivities.COLUMN_DESCRICAO} TEXT,
                ${TableActivities.COLUMN_RESPONSAVEL} TEXT,
                ${TableActivities.COLUMN_ORCAMENTO} REAL,
                ${TableActivities.COLUMN_DATA_INICIO} TEXT,
                ${TableActivities.COLUMN_DATA_FIM} TEXT,
                ${TableActivities.COLUMN_STATUS} INTEGER DEFAULT 0,
                ${TableActivities.COLUMN_APROVADA} INTEGER DEFAULT 0,
                ${TableActivities.COLUMN_ACAO_ID} INTEGER,
                FOREIGN KEY(${TableActivities.COLUMN_ACAO_ID}) REFERENCES ${TableActions.TABLE_NAME}(${TableActions.COLUMN_ID})
            )
        """
        db.execSQL(CREATE_ACTIVITIES_TABLE)

        // Criação da tabela de usuários
        val CREATE_USERS_TABLE = """
            CREATE TABLE ${TableUsers.TABLE_NAME} (
                ${TableUsers.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TableUsers.COLUMN_PAPEL} TEXT,
                ${TableUsers.COLUMN_SENHA} TEXT
            )
        """
        db.execSQL(CREATE_USERS_TABLE)

        // Inserção automática dos 3 papéis com senha padrão '123'
        val INSERT_DEFAULT_USERS = """
            INSERT INTO ${TableUsers.TABLE_NAME} (${TableUsers.COLUMN_PAPEL}, ${TableUsers.COLUMN_SENHA}) VALUES
            ('apoio', '123'),
            ('coordenador', '123'),
            ('gestor', '123')
        """
        db.execSQL(INSERT_DEFAULT_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${TableActivities.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${TableActions.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${TableUsers.TABLE_NAME}")
        onCreate(db)
    }
}
