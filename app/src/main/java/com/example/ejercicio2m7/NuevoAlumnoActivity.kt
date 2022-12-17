package com.example.ejercicio2m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ejercicio2m7.databinding.ActivityNuevoAlumnoBinding
import org.json.JSONArray
import org.json.JSONObject

class NuevoAlumnoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevoAlumnoBinding
    private lateinit var volleyAPI: VolleyAPI
    private  var nuevoAlumno= Alumno("","","", mutableListOf<Materia>())

    private lateinit var adapter: MateriasViewHolder

    private val ipPuerto="10.2.18.207:8080"
    private val localtunnel="https://server.loca.lt"  //"10.2.18.207:8080"
    private var serverOption = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        serverOption = intent.getIntExtra("serverOption",1)
        volleyAPI = VolleyAPI(this)



        binding.addAlumno.setOnClickListener() {

            val valido = validaDatosAlumno()
            if(valido){
                nuevoAlumno.edad = binding.nueoAlumnoEdad.text.toString()
                nuevoAlumno.cuenta = binding.nuevoAlumnoCuenta.text.toString()
                nuevoAlumno.nombre = binding.nueoAlumnoNombre.text.toString()
                studentsAdd(serverOption)


            }else{
                Toast.makeText(this,getString(R.string.alumnoNoValido), Toast.LENGTH_SHORT).show()
            }
        }

        binding.addMateria.setOnClickListener() {

            val valido = validaDatosMateria()
            if(valido){
                val materia = Materia(binding.nuevaMateriaId.text.toString(),binding.nuevaMateriaNombre.text.toString(),binding.nuevaMateriaCreditos.text.toString())
                nuevoAlumno.materias.add(materia)
                adapter.notifyDataSetChanged()
                binding.nuevaMateriaId.text.clear()
                binding.nuevaMateriaCreditos.text.clear()
                binding.nuevaMateriaNombre.text.clear()

            }else{
                Toast.makeText(this,getString(R.string.materiaNoValido), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initRecycler(){
        adapter=MateriasViewHolder(this,nuevoAlumno.materias)
        binding.materias.layoutManager = LinearLayoutManager(this)
        binding.materias.adapter = adapter
    }

    fun validaDatosAlumno(): Boolean{
        if(binding.nuevoAlumnoCuenta.text.toString() == "")
            return false
        if(binding.nueoAlumnoEdad.text.toString() == "")
            return false
        if(binding.nueoAlumnoNombre.text.toString() == "")
            return false

        return true

    }

    fun validaDatosMateria(): Boolean{
        if(binding.nuevaMateriaId.text.toString() == "")
            return false
        if(binding.nuevaMateriaCreditos.text.toString() == "")
            return false
        if(binding.nuevaMateriaNombre.text.toString() == "")
            return false

        return true

    }




    private fun studentsAdd(option:Int){

        var urlJSON =""
        when (option) {
            1 -> urlJSON = "http://"+ipPuerto+"/agregarestudiante"
            2 -> urlJSON = localtunnel+"/agregarestudiante"
            else -> {
                Toast.makeText(this,getString(R.string.error2),Toast.LENGTH_SHORT).show()
            }
        }


        val jsonRequest = object : JsonArrayRequest(
            urlJSON,
            Response.Listener<JSONArray> { response ->
                finish();
            },
            Response.ErrorListener {
                it.printStackTrace()
                println(it.toString())
                Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                return headers
            }
            override fun getBody(): ByteArray {
                val estudiante = JSONObject()
                estudiante.put("cuenta",nuevoAlumno.cuenta)
                estudiante.put("nombre",nuevoAlumno.nombre)
                estudiante.put("edad",nuevoAlumno.edad)
                val materias=JSONArray()
                //var itemMaterias=JSONObject()

                nuevoAlumno.materias.forEach {
                    var itemMaterias=JSONObject()
                    itemMaterias.put("id",it.id)
                    itemMaterias.put("nombre",it.nombre)
                    itemMaterias.put("creditos",it.creditos)
                    materias.put(itemMaterias)
                }


                estudiante.put("materias",materias)
                println(estudiante.toString())
                return estudiante.toString().toByteArray(charset = Charsets.UTF_8)

            }
            override fun getMethod(): Int {
                return Method.POST
            }
        }
        volleyAPI.add(jsonRequest)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}