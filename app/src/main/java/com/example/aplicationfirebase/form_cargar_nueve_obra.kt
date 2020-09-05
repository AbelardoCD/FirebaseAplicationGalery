package com.example.aplicationfirebase

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicationfirebase.models.Obra
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_form_cargar_nueve_obra.*
import java.io.IOException


class form_cargar_nueve_obra : AppCompatActivity() {
    lateinit var img: ImageButton
    private val photo: Int = 1
    lateinit var filpath: Uri
    lateinit var firebaseStorage: StorageReference
    lateinit var nombre: String
    lateinit var autor: String
    lateinit var descripcion: String
    lateinit var mdatabase: DatabaseReference
    lateinit var conFire: coneccionFirebase
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cargar_nueve_obra)
        img = findViewById(R.id.imgBtn)
        auth = FirebaseAuth.getInstance()
        mdatabase = FirebaseDatabase.getInstance().getReference()
        firebaseStorage = FirebaseStorage.getInstance().getReference()

        conFire = coneccionFirebase(this)


        getGaleria()
        cargaImagenStorage()
    }

    private fun getGaleria() {
        imgBtn.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "selecciona una imagen"), photo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == photo && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filpath = data.data!!
            try {
                val bitmapImg: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filpath)
                img.setImageBitmap(bitmapImg)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun cargaImagenStorage(){
        btnNuevaObra.setOnClickListener {
            try {


                val refer = firebaseStorage.child("imagesObras").child(filpath.lastPathSegment!!)
                val uploadTask = refer.putFile(filpath)

                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    refer.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        println("la url ...." + downloadUri)
                        getdatosNuevObra()
                        val ref = conFire.getReferenciaObras()
                        val idObra = ref.push().key
                        val nuevaObra = Obra(idObra!!,nombre,autor,descripcion,
                            downloadUri.toString()
                        )

                        println("Nueva Obra..." + nuevaObra)
                        val conexion = conFire
                        val pasarParametrosConexion = conexion.guardarObra(nuevaObra)
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }catch (e:IOException){
                e.printStackTrace()
            }
        }

    }

    private fun getdatosNuevObra() {
        nombre =txtNombreObra.text.toString()
        autor = txtAutorObra.text.toString()
        descripcion =txtDescripcionObra.text.toString()
    }

    private fun cargarNuevaObra() {


        btnNuevaObra.setOnClickListener {


            nombre = txtNombreObra.text.toString()
            autor = txtAutorObra.text.toString()
            descripcion = txtDescripcionObra.text.toString()


            val stRef: StorageReference =
                firebaseStorage.child("fotosObras").child(filpath.lastPathSegment!!)
            val upldTask: UploadTask = stRef.putFile(filpath)
            println("desde task ..........." + upldTask)


        }
    }

    private fun cargarObra() {
        btnNuevaObra.setOnClickListener {
            nombre = txtNombreObra.text.toString()
            autor = txtAutorObra.text.toString()
            descripcion = txtDescripcionObra.text.toString()

            val ref = conFire.getReferenciaObras()
            val idObra = ref.push().key

            //conFire.guardarObra(idObra!!, nombre, autor, descripcion, filpath)


        }
    }

}

