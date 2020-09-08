package com.example.aplicationfirebase.models

class TemasPojo
{
    var idTema:String = ""
        //get() = field
    var Nombre:String=""
        //get() = field
    var Descripcion:String=""
        //get() = field
    var url:String=""
      //  get() = field
    var obrasAsignadas:HashMap<String,Boolean>?=HashMap()

    constructor(){}

    constructor(idtema:String,nombre:String,descripcion:String,url:String){
        this.idTema=idtema
        this.Nombre=nombre
        this.Descripcion=descripcion
        this.url = url
        this.obrasAsignadas =   HashMap()
    }

    public override fun toString():String{
        return Nombre
    }




}