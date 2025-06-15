package com.example.pi3.coordenador

import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pi3.R
import com.google.android.material.bottomnavigation.BottomNavigationView

//classe que renderiza o menu e as páginas do fluxo do corrdenador
class CoordenadorActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordenador)

        // Obtém o NavHostFragment e o NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configura o BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Configura a navegação com o NavController
        bottomNavigationView.setupWithNavController(navController)

        // Definir o fragmento inicial
    }



}