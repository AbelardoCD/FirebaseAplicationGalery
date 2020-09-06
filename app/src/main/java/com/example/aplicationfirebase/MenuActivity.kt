package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.example.aplicationfirebase.models.userPreferenceManager
import kotlinx.android.synthetic.main.menu.*

class MenuActivity : AppCompatActivity() {
    lateinit var btnGaleria: Button
    lateinit var btnNuevo: Button
    lateinit var userPreference: userPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN)
                           */

         setContentView(R.layout.menu)



        btnGaleria = findViewById(R.id.btnIrGaleria)
        btnNuevo = findViewById(R.id.btnIrVentanaNuevaObra)

        userPreference = userPreferenceManager(this)
        btnGaleria.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }


        btnNuevo.setOnClickListener {
            val intent = Intent(this, form_cargar_nueve_obra::class.java)
            startActivity(intent)
        }

        btncerrarSesion.setOnClickListener {
            val cerrasrSesion = userPreference.deleteUser()
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }


}