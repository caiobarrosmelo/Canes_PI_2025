package com.seuapp.tela

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.seuapp.R

class GestorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atividade_gestor)

        val btnUsuarios = findViewById<Button>(R.id.btnUsuarios)
        val btnNotificacoes = findViewById<Button>(R.id.btnNotificacoes)
        val btnConfiguracoes = findViewById<Button>(R.id.btnConfiguracoes)

        btnUsuarios.setOnClickListener {
            startActivity(Intent(this, UsuariosActivity::class.java))
        }

        btnNotificacoes.setOnClickListener {
            startActivity(Intent(this, NotificacoesActivity::class.java))
        }

        btnConfiguracoes.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }
    }
}
