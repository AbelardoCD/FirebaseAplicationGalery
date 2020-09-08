package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.TemasPojo
import com.example.aplicationfirebase.models.User
import com.example.aplicationfirebase.models.userPreferenceManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.menu.*
import kotlinx.android.synthetic.main.menu.btncerrarSesion

class MenuActivity : AppCompatActivity() {
    lateinit var btnGaleria: Button
    lateinit var btnNuevo: Button
    lateinit var userPreference: userPreferenceManager

    lateinit var database: FirebaseDatabase
    lateinit var refrence: DatabaseReference
    var idMovimiento: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        btnGaleria = findViewById(R.id.btnIrGaleria)
        btnNuevo = findViewById(R.id.btnIrVentanaNuevaObra)

        userPreference = userPreferenceManager(this)

        database = FirebaseDatabase.getInstance()
        refrence = database.getReference("movimiento")

        var userLog = intent.extras?.getString("usuarioLogeado")
        userrTxt.text = userLog
        idMovimiento = intent.extras?.getString("idMovimiento").toString()
        datosMovimiento()
    }

    private fun datosMovimiento() {
        println("Desde Usuario log  " + idMovimiento)


        val query: Query = refrence.orderByChild("idTema").equalTo(idMovimiento)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("snaps    " + snapshot)
                for (data in snapshot.children) {
                    val mov = data.getValue(TemasPojo::class.java)
                    titulomovtxt.text = mov?.Nombre
                    descripciontxt.text = mov?.Descripcion
                   // Glide.with(this@MenuActivity).load(mov?.url).centerCrop().into(imgDescripcionMovimiento)
                }


            }

        })


    }

    fun butonControl() {

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