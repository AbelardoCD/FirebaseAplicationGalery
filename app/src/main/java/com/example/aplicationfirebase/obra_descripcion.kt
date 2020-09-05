package com.example.aplicationfirebase

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.Obra
import com.google.firebase.database.*
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_obra_descripcion.*

class obra_descripcion : AppCompatActivity() {

    lateinit var coneccion: coneccionFirebase
    lateinit var referencia:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obra_descripcion)

        coneccion = coneccionFirebase(this)
        referencia = coneccion.getReferenciaObras()

        getDatosObra()

    }
    private fun getDatosObra(){
        referencia.child(obtenemosId()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("objeto conmpleto.........." + snapshot)
                val obra = snapshot.getValue(Obra::class.java)!!
                mostrarDatos(obra.nombre,obra.autor,obra.descripcion,obra.url)
            }

        })
    }
    public fun mostrarDatos(nombre:String,autor:String,descripcionObra:String,url:String){
        txtTituloObraDescripcion.text = nombre
        txtDescripcionDescripcion.text = descripcionObra
        txtAutorObraDescripcion.text = autor
        Glide.with(this).load(url).into(imgObraDescripcion)


       /* Blurry.with(this)
            .radius(25)
            .sampling(1)
            .color(Color.argb(66, 0, 255, 255))
            .async()
            .capture(findViewById(R.id.imgObraDescripcion))
            .into(findViewById(R.id.imgObraDescripcion))
    */
    }

    private fun obtenemosId():String{
        val nombreObra = intent.extras
        val id = nombreObra?.getString("id")
        return id.toString()
    }

}