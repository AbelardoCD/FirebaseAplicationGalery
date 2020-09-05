package com.example.aplicationfirebase.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.Obra
import kotlinx.android.synthetic.main.item_obra.view.*


class obraAdapter(private val mContext: Context,private val listaObras:List<Obra>): ArrayAdapter<Obra>(mContext,0,listaObras)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layout = LayoutInflater.from(context).inflate(com.example.aplicationfirebase.R.layout.item_obra,parent,false)
        val obras = listaObras[position]
        layout.txtNombreObraEnLista.text = obras.nombre
        layout.txtautorListView.text = obras.autor
        val img: ImageView =layout.imgViewObra
        //println("img desd adapter......."  + obras.nombre + "    filpath" + obras.url)
        Glide.with(mContext)
            .load(obras.url)
            .centerCrop()
            .into(img)

        return layout

    }


}