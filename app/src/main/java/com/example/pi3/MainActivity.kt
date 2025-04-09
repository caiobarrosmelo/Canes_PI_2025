package com.example.pi3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btLogin: Button = findViewById(R.id.entrar_btn)
        btLogin.setOnClickListener(onClickLogin())
    }

    private fun onClickLogin(): View.OnClickListener {
        return View.OnClickListener { v ->
            val tLogin: TextView = findViewById(R.id.login_input)
            val tSenha: TextView = findViewById(R.id.senha_input)
            val login = tLogin.text.toString()
            val senha = tSenha.text.toString()

            if (login == "teste" && senha == "123") {
                // Navega para a próxima tela
                val intent = Intent(getContext(), Analista::class.java)
                startActivity(intent)
            } else {
                alert("Login e senha incorretos.")
            }
        }
    }

    private fun getContext(): Context {
        return this
    }

    private fun alert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

        }