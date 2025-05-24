package com.example.pi3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3.apoio.ApoioActivity
import com.example.pi3.coordenador.CoordenadorActivity
import com.example.pi3.gestor.Gestor
import com.example.pi3.data.UserRepository


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
        return View.OnClickListener {
            val tLogin: EditText = findViewById(R.id.login_input)
            val tSenha: EditText = findViewById(R.id.senha_input)
            val login = tLogin.text.toString().trim()
            val senha = tSenha.text.toString().trim()

            if (login.isEmpty() || senha.isEmpty()) {
                alert("Por favor, preencha todos os campos.")
                return@OnClickListener
            }

            val userRepository = UserRepository(this)
            val papel = userRepository.loginUsers(login, senha)

            when (papel) {
                "apoio" -> startActivity(Intent(this, ApoioActivity::class.java))
                "coordenador" -> startActivity(Intent(this, CoordenadorActivity::class.java))
                "gestor" -> startActivity(Intent(this, Gestor::class.java))
                else -> alert("Login e/ou senha incorretos.")
            }
        }
    }

    private fun alert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
