package com.example.aplicationfirebase.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.aplicationfirebase.models.Obra
import com.example.aplicationfirebase.models.TemasPojo
import kotlinx.android.synthetic.main.item_obra.view.*

class AdapterMovimientos(private val mContext: Context, private val listaMovimientos:List<TemasPojo>):
    ArrayAdapter<TemasPojo>(mContext,0,listaMovimientos)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        val layout = LayoutInflater.from(context).inflate(com.example.aplicationfirebase.R.layout.item_obra,parent,false)
        val movimiento = listaMovimientos[position]
        layout.txtNombreObraEnLista.text = movimiento.Nombre
        //layout.txtautorListView.text = obras.autor
        val img: ImageView =layout.imgViewObra
        //println("img desd adapter......."  + obras.nombre + "    filpath" + obras.url)
        Glide.with(mContext)
            .load(movimiento.url)
            .centerCrop()
            .into(img)

        return layout
    }
}