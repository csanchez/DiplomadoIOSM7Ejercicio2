package com.example.ejercicio2m7

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class MateriasViewHolder (private val context: Context, private val materias: List<Materia>) :

    RecyclerView.Adapter<MateriasViewHolder.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var materiaInfo:TextView
        init {
            materiaInfo=view.findViewById(R.id.materiaInfo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li=LayoutInflater.from(parent.context)
        val v=li.inflate(R.layout.materias_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materia=materias[position]
        holder.materiaInfo.text=materia.id+ " - "+materia.creditos+" - "+materia.nombre
    }
    override fun getItemCount(): Int {
        return materias.size
    }
}