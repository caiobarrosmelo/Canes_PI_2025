package com.example.pi3.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.pi3.R


abstract class function_arrow_back : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o botão de voltar
        val backButton: ImageButton? = view.findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}