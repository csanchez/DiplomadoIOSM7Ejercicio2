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

class AlumnosViewHolder (private val context: Context, private val alumnos: List<Alumno>) :

    RecyclerView.Adapter<AlumnosViewHolder.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var alumnoInfo:TextView
        //var cuenta:TextView
        //var edad:TextView
        init {
            alumnoInfo=view.findViewById(R.id.alumnoInfo)
            //cuenta=view.findViewById(R.id.alumnoCuenta)
            //edad=view.findViewById(R.id.alumnoEdad)

        }
    }
    //pasara el layout a viewholder para el pintado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li=LayoutInflater.from(parent.context)
        val v=li.inflate(R.layout.alumno,parent,false)
        return ViewHolder(v)
    }

    //este realizara el pinta de cada elemento
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alumno=alumnos[position]
        holder.alumnoInfo.text=alumno.cuenta+ " - "+alumno.nombre
        //holder.edad.text=alumno.edad
        //holder.cuenta.text=alumno.cuenta

       // holder.image.setImageResource(R.drawable.tu_imagen)
        /*holder.contraint.setOnClickListener{
            Toast.makeText(context,"Item" + data[position].name,Toast.LENGTH_SHORT).show()
        }*/
    }
    override fun getItemCount(): Int {
        return alumnos.size
    }
}