package com.example.aplicationfirebase

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicationfirebase.Adapters.obraAdapter
import com.example.aplicationfirebase.models.Obra
import com.example.aplicationfirebase.models.User
import com.example.aplicationfirebase.models.userPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btncerrarSesion
import kotlinx.android.synthetic.main.menu.*
import java.io.IOException


enum class providerType {
    BASIC
}

class Home : AppCompatActivity() {
    private lateinit var Auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    /*********************************************/
    lateinit var database: FirebaseDatabase
    lateinit var referencia: DatabaseReference

    lateinit var obrasList: MutableList<Obra>
    lateinit var lista: ListView

    lateinit var userPreference:userPreferenceManager
     lateinit var btnGaleria: Button
    lateinit var btnCerrarSesion : Button
    lateinit var btnNuevo: Button
    /********************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        cerrarSesion()
        userPreference = userPreferenceManager(this)

        btnGaleria =findViewById(R.id.btnIrGaleria)
        btnNuevo =findViewById(R.id.btnIrVentanaNuevaObra)
        btnCerrarSesion = findViewById(R.id.btncerrarSesion)
        controlBotones()

        Auth = FirebaseAuth.getInstance()
        user = Auth.currentUser!!
        datosUsuarioLogeado(user)
        /****************************/
        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("obras")




        obrasList = mutableListOf()
        lista = findViewById(R.id.lisVObras)


        getObras()

        itemClic()
    }

    private fun datosUsuarioLogeado(userMail: FirebaseUser) {
        println("Desde Usuario log")

        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("User")
        val query: Query = referencia.orderByChild("Email").equalTo(userMail.email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {


                for (dato in snapshot.children) {
                    val user = dato.getValue(User::class.java)
                    txtEmailUser.text = user?.Nombre
                }

            }

        })


    }

    fun getObras() {
        println("INICAIAMOS...........")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    obrasList.clear()
                    for (dato in snapshot.children) {
                        val nuevaObra = dato.getValue(Obra::class.java)
                        obrasList.add(nuevaObra!!)


                    }

                }
                val adp = obraAdapter(this@Home, obrasList)
                lista.adapter = adp


            }

        })

    }

    private fun itemClic() {
        lista.setOnItemClickListener { adapterView, view, i, l ->

            Toast.makeText(this, obrasList[i].nombre, Toast.LENGTH_LONG).show()
            val intent = Intent(this, obra_descripcion::class.java).apply {
                putExtra("id", obrasList[i].id)
            }
            startActivity(intent)
        }


    }

    private fun cerrarSesion() {
        //    btnCerrarSesion.setOnClickListener {
        //      FirebaseAuth.getInstance().signOut()

        //    onBackPressed()
        //}
    }

    /*Traemos todos los usuarrios y comparamos*/
    private fun datosUsuario(userMail: FirebaseUser) {
        println("Desde Usuario")

        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("User")
        //val query:Query = referencia

        referencia.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("Desde snap" + snapshot)
                for (dato in snapshot.children) {

                    val user = dato.getValue(User::class.java)
                    println("user" + user?.Email)
                    if (userMail.email.equals(user?.Email)) {
                        txtEmailUser.text = user?.Nombre
                    }

                }

            }


        })
    }

    private fun controlBotones() {

        btnGaleria.setOnClickListener {
           Toast.makeText(this,"Estamos en Galeria!",Toast.LENGTH_LONG).show()
        }


        btnNuevo.setOnClickListener {
            val intent = Intent(this, form_cargar_nueve_obra::class.java)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val cerrasrSesion = userPreference.deleteUser()
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}
