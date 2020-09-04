package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


        lateinit var progresBar:ProgressBar
        lateinit var database :FirebaseDatabase
        lateinit var referencia:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ventanaHome()
        progresBar = findViewById(R.id.progressBarCarga)


        database = Firebase.database
        referencia = database.reference.child("User")

    }


    private fun ventanaHome(){
        btnRegistrar.setOnClickListener {
            if(txtemail.text.isNotEmpty() && txtPassword.text.isNotEmpty() && txtNombre.text.isNotEmpty() && txtApellido.text.isNotEmpty()){
                progresBar.visibility = View.VISIBLE
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtemail.text.toString(),txtPassword.text.toString()).addOnCompleteListener {
                    if(it.isComplete){
                        val user = FirebaseAuth.getInstance().currentUser!!
                        verificarEmail(user)
                        val userDB = referencia.child(user.uid)



                        userDB.child("Nombre").setValue(txtNombre.text.toString())
                        userDB.child("Apellido").setValue(txtApellido.text.toString())
                        userDB.child("Email").setValue(txtemail.text.toString())

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
        val intent =Intent(this,login::class.java)
        startActivity(intent)
    }
    private  fun verificarEmail(user:FirebaseUser?) {
    user?.sendEmailVerification()
        ?.addOnCompleteListener {
            if (it.isComplete) {
                Toast.makeText(this, "Correo enviado", Toast.LENGTH_LONG)
            } else {
                Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_LONG)

            }
        }
}
}

