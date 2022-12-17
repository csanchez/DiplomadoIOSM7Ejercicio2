package com.example.ejercicio2m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ejercicio2m7.databinding.ActivityMainBinding
import com.example.ejercicio2m7.databinding.ActivityVerAlumnoBinding

import org.json.JSONObject

class VerAlumnoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerAlumnoBinding
    private lateinit var volleyAPI: VolleyAPI



    private var ipPuerto=""
    private var localtunnel=""  //"10.2.18.207:8080"
    private var serverOption = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerAlumnoBinding.inflate(layoutInflater)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}