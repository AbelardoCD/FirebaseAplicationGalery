package com.example.aplicationfirebase

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.Obra
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_obra_descripcion.*
import kotlinx.android.synthetic.main.popup.view.*


class obra_descripcion : AppCompatActivity() {

    lateinit var coneccion: coneccionFirebase
    lateinit var referencia: DatabaseReference
    lateinit var popUp: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_descripcion)




        coneccion = coneccionFirebase(this)
        referencia = coneccion.getReferenciaObras()


        //  popUp = Dialog(this)

        getDatosObra()


    }

    private fun getDatosObra() {
        referencia.child(obtenemosId()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("objeto conmpleto.........." + snapshot)
                val obra = snapshot.getValue(Obra::class.java)!!
                mostrarDatos(obra.nombre, obra.autor, obra.descripcion, obra.url)
            }

        })
    }

    public fun mostrarDatos(nombre: String, autor: String, descripcionObra: String, url: String) {
        txtTituloObraDescripcion.text = nombre
        txtDescripcionDescripcion.text = descripcionObra
        txtAutorObraDescripcion.text = autor

        Glide.with(this).load(url).thumbnail().into(imgObraDescripcion)


        popUp(url)

    }

    public fun popUp(url: String) {
        imgObraDescripcion.setOnClickListener {
            val dialog = LayoutInflater.from(this).inflate(R.layout.popup, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialog)
            val alertDialog = builder.show()
            val img = dialog.imgDescripcionPopUp
            Glide.with(this).load(url).into(img)

        }
    }

    private fun obtenemosId(): String {
        val nombreObra = intent.extras
        val id = nombreObra?.getString("id")
        return id.toString()
    }

}



