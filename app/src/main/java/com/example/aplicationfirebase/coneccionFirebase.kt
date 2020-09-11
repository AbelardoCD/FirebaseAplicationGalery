package com.example.aplicationfirebase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.aplicationfirebase.models.Obra
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class coneccionFirebase {
    lateinit var mContext: Context

    lateinit var database: FirebaseDatabase
    lateinit var referencia: DatabaseReference


    constructor(mcontext: Context) {
        this.mContext = mcontext
    }


    public fun guardarObra(obra: Obra) {


        try {
            var hashMap: HashMap<String, String> = HashMap<String, String>()
            hashMap.put("id", obra.id)
            hashMap.put("idMovimiento", obra.idMovimiento)

            hashMap.put("nombre", obra.nombre)
            hashMap.put("autor", obra.autor)
            hashMap.put("descripcion", obra.descripcion)
            hashMap.put("url", obra.url.toString())




            getReferenciaObras().child(obra.id).setValue(hashMap)
                .addOnCompleteListener {
                   // Toast.makeText(mContext, "Cargado exitosamente.", Toast.LENGTH_LONG).show()
                    println("CARGADO EXITOSAMENTE...")
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    public fun getReferenciaObras(): DatabaseReference {
        inicializarBasedeDatos()
        return referencia.child("obras")
    }

    private fun inicializarBasedeDatos() {
        database = FirebaseDatabase.getInstance()
        referencia = database.getReference()


    }
}

