package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.Obra

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.txtNombre

class login : AppCompatActivity() {
    lateinit var progresBar:ProgressBar
    /*******************************************/

    lateinit var database:FirebaseDatabase
    lateinit var referencia:DatabaseReference
    lateinit var listaObras: ArrayList<Obra>
        lateinit var obr:Obra
    /*******STORAGE*****/
    lateinit var storage:StorageReference
     lateinit var img:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ventanaRegistro()
        login()
        progresBar = progressBar
        /*******************************************/
        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("obras")

        listaObras = ArrayList()

        img =findViewById(R.id.imgpruebaConsulta)
  consultarImagen()
    }
    fun getRegistro(){
        referencia.child("qwerty").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("objeto conmpleto.........." + snapshot)
               obr = snapshot.getValue(Obra::class.java)!!

                //txtNombreObra.text = obr.nombre
            }

        })
    }

 fun consultarImagen(){
       // val storage = Firebase.storage.getReference("https://firebasestorage.googleapis.com/v0/b/aplicationfirebase-62637.appspot.com/o/imagesObras%2Fimage%3A137705?alt=media&token=80a3cb24-6242-410e-aa6c-a696c0af325a")
        //println("storage....."+ storage)
        Glide.with(this).
        load("https://firebasestorage.googleapis.com/v0/b/aplicationfirebase-62637.appspot.com/o/imagesObras%2Fimage%3A137705?alt=media&token=80a3cb24-6242-410e-aa6c-a696c0af325a")
            .into(img)
 }


    private fun ventanaRegistro(){
        btnVentanaRegistrarse.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(){
        btnAccederHome.setOnClickListener {
            if(txtEmail.text.isNotEmpty() && txtPasswordLogin.text.isNotEmpty() ){
                progresBar.visibility = View.VISIBLE
                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmail.text.toString(),txtPasswordLogin.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        logSuccess(it.result?.user?.email ?:"",providerType.BASIC)
                    }else{
                        alertaErrorAutenticando()
                    }
                }
            }else{
                errorCamposIncompletos()
            }
        }
    }

    private fun errorCamposIncompletos(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Los campos deben estar llenos!!")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun alertaErrorAutenticando(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun logSuccess(email:String, provider:providerType){
        progresBar.visibility = View.INVISIBLE
        val intent =Intent(this,Home::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
    }
}