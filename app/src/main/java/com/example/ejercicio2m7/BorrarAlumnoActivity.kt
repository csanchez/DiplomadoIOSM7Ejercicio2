package com.example.ejercicio2m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ejercicio2m7.databinding.ActivityBorrarAlumnoBinding
import org.json.JSONArray
import org.json.JSONObject

class BorrarAlumnoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBorrarAlumnoBinding
    private lateinit var volleyAPI: VolleyAPI



    private var ipPuerto=""
    private var localtunnel=""  //"10.2.18.207:8080"
    private var serverOption = 1
    private   var alumnoSelected = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBorrarAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)



        serverOption = intent.getIntExtra("serverOption",1)
        ipPuerto = intent.getStringExtra("ipPuerto").toString()
        localtunnel =  intent.getStringExtra("localtunnel").toString()

        volleyAPI = VolleyAPI(this)



        binding.buscar.setOnClickListener {
            studentsID(serverOption)
        }

        binding.borrar.setOnClickListener {
            studentsDelete(serverOption)
        }

    }



    private fun studentsID(option: Int){
        var urlJSON = ""
        when (option) {
            1 -> urlJSON = "http://"+ipPuerto+"/id/"+binding.searchText.text.toString()
            2 -> urlJSON = localtunnel+"/id/"+binding.searchText.text.toString()
            else -> {
                Toast.makeText(this,getString(R.string.error2), Toast.LENGTH_SHORT).show()
            }
        }

        var cadena = ""
        val jsonRequest = object : JsonObjectRequest(
            Method.GET,
            urlJSON,
            null,
            Response.Listener<JSONObject> { response ->
                println(response)
                val materia=response.getJSONArray("materias")

                binding.userFound.text = response.get("cuenta").toString() + " - "+ response.get("nombre").toString()+ " - "+ response.get("edad").toString() +"\n "+response.getJSONArray("materias").toString()
                alumnoSelected = response.get("cuenta").toString()
            },
            Response.ErrorListener {
                binding.userFound.text = ""
                Toast.makeText(this,getString(R.string.noEncontrado), Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
        }
        volleyAPI.add(jsonRequest)
    }

    private fun studentsDelete(option: Int) {

        var urlJSON = ""
        when (option) {
            1 -> urlJSON = "http://"+ipPuerto+"/borrarestudiante/"+binding.searchText.text.toString()
            2 -> urlJSON = localtunnel+"/borrarestudiante/"+binding.searchText.text.toString()
            else -> {
                Toast.makeText(this,getString(R.string.error2),Toast.LENGTH_SHORT).show()
            }
        }
        val jsonRequest = object : JsonArrayRequest(
            urlJSON,
            Response.Listener<JSONArray> {
                binding.userFound.text = ""
                Toast.makeText(this,getString(R.string.borrado),Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
            override fun getMethod(): Int {return return Method.DELETE
            }
        }
        volleyAPI.add(jsonRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}