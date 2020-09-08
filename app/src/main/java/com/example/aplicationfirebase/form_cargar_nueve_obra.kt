package com.example.aplicationfirebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.withStyledAttributes
import androidx.core.view.get
import com.example.aplicationfirebase.Adapters.obraAdapter
import com.example.aplicationfirebase.models.Obra
import com.example.aplicationfirebase.models.TemasPojo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
    lateinit var tema: String
    lateinit var mdatabase: DatabaseReference
    lateinit var conFire: coneccionFirebase
    lateinit var auth: FirebaseAuth

    /**SPINER***/
    lateinit var spinerCombo: Spinner
    lateinit var listaTemas: MutableList<TemasPojo>
    var adapterArray: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cargar_nueve_obra)
        img = findViewById(R.id.imgBtn)
        auth = FirebaseAuth.getInstance()
        mdatabase = FirebaseDatabase.getInstance().getReference()
        firebaseStorage = FirebaseStorage.getInstance().getReference()

        conFire = coneccionFirebase(this)


        getGaleriatelefono()
        guardarObra()

        /********SPINNER*******/
        spinerCombo = findViewById(R.id.comboTemasSpn)
        listaTemas = mutableListOf()


        getTemas()

    }

    private fun getGaleriatelefono() {
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

    private fun guardarObra() {
        getdatosNuevObra()
        btnNuevaObra.setOnClickListener {

            try {
                if(nombre.isNotEmpty() && autor.isNotEmpty() && descripcion.isNotEmpty() &&  tema =="Selecciona" ){

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
                        //PREPARAMOS DE LA OBRRA PARA GUARDARLOS

                        val ref = conFire.getReferenciaObras()
                        val idObra = ref.push().key
                        val nuevaObra = Obra(
                            idObra!!, nombre, autor, descripcion,
                            downloadUri.toString()
                        )


                        println("Nueva Obra..." + nuevaObra)
                        val conexion = conFire

                        val pasarParametrosConexion = conexion.guardarObra(nuevaObra)
                        //ASIGNAMOS LA OBRA A UN TEMA CON EL SIGUIENTE METODO

                        asignarUnaObraATema(tema, idObra)


                    } else {
                        // Handle failures
                        // ...
                    }
                }
                }else{
                    Toast.makeText(this,"Debes completar todos los campos",Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun getdatosNuevObra() {
        nombre = txtNombreObra.text.toString()
        autor = txtAutorObra.text.toString()
        descripcion = txtDescripcionObra.text.toString()

    }

    fun getTemas() {
        var objetoInstruccion = TemasPojo("Selecciona", "Selecciona","Selecciona","Selecciona")
        println("combo temas")
        val referenciaTemas = mdatabase.child("movimiento")

        referenciaTemas.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")

            }


            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listaTemas.clear()
                    listaTemas.add(objetoInstruccion)
                    for (data in snapshot.children) {

                        val nuevotema = data.getValue(TemasPojo::class.java)
                        println("pintamos los temas   " + nuevotema?.Nombre)

                        listaTemas.add(nuevotema!!)
                    }
                }
                val adapter = ArrayAdapter(
                    this@form_cargar_nueve_obra, android.R.layout.simple_spinner_dropdown_item
                    , listaTemas
                )
                spinerCombo.adapter = adapter
            }


        })
        itemTemaOnclic()
    }


    fun itemTemaOnclic() {
        spinerCombo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              //  Toast.makeText(this@form_cargar_nueve_obra,listaTemas[position].idTema +"    "+listaTemas[position].Nombre ,Toast.LENGTH_LONG).show()
                tema =listaTemas[position].idTema

            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    fun asignarUnaObraATema(idTema:String,idObraAsignada:String){
        println("Entramos a la asignacion")
        mdatabase.child("movimiento").child(idTema).child("obrasAsignadas").child(idObraAsignada).setValue(true).addOnCompleteListener {
            println("Asignado correctamente.......FIN")
            Toast.makeText(this,"Guardado correctamente",Toast.LENGTH_LONG).show()
            val intent = Intent(this,Home::class.java)

        }

    }

    /************METODO QUE SE USARON COMO PRUEBA SE DEJAN COMO EJEMPLO EN CASO DE NECESITARLOS**********************/
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

