package com.example.aplicationfirebase

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.aplicationfirebase.models.Obra
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.HashMap

class coneccionFirebase {
    lateinit var mContext: Context
    lateinit var auth: FirebaseAuth

    lateinit var referencia: DatabaseReference
    lateinit var storageRef: StorageReference


    constructor(
        mcontext: Context,
        auth: FirebaseAuth,

        referencia: DatabaseReference,
        storageRef: StorageReference
    ) {
        this.auth = auth
        this.mContext = mcontext
        this.referencia = referencia
        this.storageRef = storageRef
    }


    public fun guardarObra(obra:Obra){

        println("obra desde conecccion..." + obra.id +" "+ obra.nombre +" "+ obra.autor +" "+ obra.descripcion +" "+ obra.filpath)
        Toast.makeText(mContext, "La URL es:  " + "obra desde conecccion..." + obra.id +" "+ obra.nombre +" "+ obra.autor +" "+ obra.descripcion +" "+ obra.filpath, Toast.LENGTH_LONG).show()

        var hashMap : HashMap<String, String>
                = HashMap<String, String> ()
        hashMap.put("id",obra.id)
        hashMap.put("nombre",obra.nombre)
        hashMap.put("autor",obra.autor)
        hashMap.put("descripcion",obra.descripcion)
        hashMap.put("url", obra.filpath.toString())



        // val re = getReferenciaObras()
        referencia.child("obras").child(obra.id).setValue(hashMap).addOnCompleteListener {
            Toast.makeText(mContext, "Cargado exitosamente." , Toast.LENGTH_LONG).show()

        }
    }


    public fun getReferenciaObras(): DatabaseReference {

        return referencia.child("obras")
    }
}

