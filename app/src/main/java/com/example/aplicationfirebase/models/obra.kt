package com.example.aplicationfirebase.models

import android.net.Uri

class Obra(
    val id: String, val nombre: String,
    val autor: String,
    val descripcion: String,
    val filpath: Uri?
) {
    constructor() : this("", "", "", "", Uri.EMPTY)

}



