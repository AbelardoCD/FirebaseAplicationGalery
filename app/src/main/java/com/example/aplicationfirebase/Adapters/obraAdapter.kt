package com.example.aplicationfirebase.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.R
import com.example.aplicationfirebase.models.Obra
import kotlinx.android.synthetic.main.item_obra.view.*

class obraAdapter(private val mContext: Context,private val listaObras:List<Obra>): ArrayAdapter<Obra>(mContext,0,listaObras)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layout = LayoutInflater.from(context).inflate(com.example.aplicationfirebase.R.layout.item_obra,parent,false)
        val obras = listaObras[position]
        layout.txtNombre.text = obras.nombre



        return layout
    }
}