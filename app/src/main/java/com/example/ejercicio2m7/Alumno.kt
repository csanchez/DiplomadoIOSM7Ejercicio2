package com.example.ejercicio2m7

data class Alumno(
    var cuenta:String = "",
    var nombre:String = "",
    var edad:String = "",
    var materias: MutableList<Materia>

)
