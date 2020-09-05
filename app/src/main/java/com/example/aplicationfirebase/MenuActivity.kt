package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.menu.*

class MenuActivity : AppCompatActivity() {
    lateinit var btnGaleria: Button
    lateinit var btnNuevo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        btnGaleria = findViewById(R.id.btnIrGaleria)
        btnNuevo = findViewById(R.id.btnIrVentanaNuevaObra)

        btnGaleria.setOnClickListener {
            val intent =Intent(this,Home::class.java)
            startActivity(intent)
        }


        btnNuevo.setOnClickListener {
            val intent =Intent(this,form_cargar_nueve_obra::class.java)
            startActivity(intent)
        }
    }

}