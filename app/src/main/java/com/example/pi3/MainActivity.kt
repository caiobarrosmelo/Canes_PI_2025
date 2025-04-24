package com.example.pi3


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
import com.example.pi3.apoio.Analista
import com.example.pi3.coordenador.CoordenadorActivity
import com.example.pi3.gestor.Gestor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Lógica de padding para a tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botão de login
        val btLogin: Button = findViewById(R.id.entrar_btn)
        btLogin.setOnClickListener(onClickLogin())
    }

    private fun onClickLogin(): View.OnClickListener {
        return View.OnClickListener { v ->
            val tLogin: TextView = findViewById(R.id.login_input)
            val tSenha: TextView = findViewById(R.id.senha_input)
            val login = tLogin.text.toString()
            val senha = tSenha.text.toString()

            // Verificação simples de login e senha
            if (login == "analista" && senha == "123") {
                // Navega para a tela do Analista
                val intent = Intent(this@MainActivity, Analista::class.java)
                startActivity(intent)
            } else if (login == "coordenador" && senha == "123") {
                // Navega para a tela do Coordenador
                val intent = Intent(this@MainActivity, CoordenadorActivity::class.java)
                startActivity(intent)
            } else if (login == "gestor" && senha == "123") {
                // Navega para a tela do Gestor
                val intent = Intent(this@MainActivity, Gestor::class.java)
                startActivity(intent)
            } else {
                alert("Login e senha incorretos.")
            }
        }
    }

    private fun alert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
