package com.example.aplicationfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.aplicationfirebase.Adapters.obraAdapter
import com.example.aplicationfirebase.models.Obra
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

import kotlinx.android.synthetic.main.activity_main.*


enum class providerType{
    BASIC
}

class Home : AppCompatActivity() {
  //  private lateinit var Auth : FirebaseAuth
    // private lateinit var user:FirebaseUser
    /*********************************************/
    lateinit var database : FirebaseDatabase
    lateinit var referencia: DatabaseReference

    lateinit var obrasList:MutableList<Obra>
    lateinit var lista:ListView

    /********************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        cerrarSesion()
      //  Auth = FirebaseAuth.getInstance()
        //user = Auth.currentUser!!
        // datos(user)
        /****************************/
        database= FirebaseDatabase.getInstance()
        referencia = database.getReference("obras")

        obrasList = mutableListOf()
        lista = findViewById(R.id.lisVObras)


    getObras()

    }
    fun getObras(){
        println("INICAIAMOS...........")
        referencia.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("desde...." + snapshot)
                if (snapshot!!.exists()){
                    obrasList.clear()
                    for (dato in snapshot.children){
                        println("dato ....." + dato)
                        val nuevaObra = dato.getValue(Obra::class.java)
                        obrasList.add(nuevaObra!!)

                    }
                }
                val adp = obraAdapter(this@Home,obrasList)
                lista.adapter = adp
            }

        })



}






   private fun datos(user:FirebaseUser){
        txtEmailUser.text =  user.email


    }

    private fun cerrarSesion(){
        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            onBackPressed()
        }
    }
}
