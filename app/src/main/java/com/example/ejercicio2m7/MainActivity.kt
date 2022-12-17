package com.example.ejercicio2m7

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.example.ejercicio2m7.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var volleyAPI: VolleyAPI
    private lateinit var url: String
    private lateinit var adapter: AlumnosViewHolder
    private lateinit var drawer: DrawerLayout


    /*

        Ocupe localtunnel para que me permitiera hacer llamadas al servidor sin problemas aún así basta con poner la variable serverOption con la IP asignada para llamar al servidor por IP y puerto de la manera vista en clase


     */
    private val ipPuerto="10.2.18.207:8080"
    private val localtunnel="https://server.loca.lt"  //"10.2.18.207:8080"
    private val serverOption = 2

    private val alumnosList = mutableListOf<Alumno>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        initRecycler()
        volleyAPI = VolleyAPI(this)
        inicioToolsBar()












    }

    override fun onResume() {
        super.onResume()
        alumnosList.clear()
        studentsJSON(serverOption)
        adapter.notifyDataSetChanged()
    }


    private fun inicioToolsBar(){
        val toolbar: Toolbar =findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        val toggle= ActionBarDrawerToggle(this, drawer,toolbar,R.string.abrir,R.string.cerrar)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.unam_32)
        iniciarNavegacionView()
    }
    private fun iniciarNavegacionView(){
        val navegacionView: NavigationView =findViewById(R.id.nav_view)
        navegacionView.setNavigationItemSelectedListener(this)
        val headerView: View = LayoutInflater.from(this).inflate(R.layout.header_main,
            navegacionView,false)
        navegacionView.addHeaderView(headerView)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.verAlumnoMenuItem->{
                val intent = Intent(this,VerAlumnoActivity::class.java)
                intent.putExtra("serverOption",serverOption)
                intent.putExtra("ipPuerto",ipPuerto)
                intent.putExtra("localtunnel",localtunnel)

                startActivity(intent)
            }
            R.id.nuevoAlumnoMenuItem->{
                val intent = Intent(this, NuevoAlumnoActivity::class.java)
                intent.putExtra("serverOption",serverOption)
                intent.putExtra("ipPuerto",ipPuerto)
                intent.putExtra("localtunnel",localtunnel)
                startActivity(intent)
            }
            R.id.borrarAlumnoMenuItem->{
                val intent = Intent(this,BorrarAlumnoActivity::class.java)
                intent.putExtra("serverOption",serverOption)
                intent.putExtra("ipPuerto",ipPuerto)
                intent.putExtra("localtunnel",localtunnel)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



    private fun initRecycler(){
        adapter=AlumnosViewHolder(this,alumnosList)
        binding.alumnos.layoutManager = LinearLayoutManager(this)
        binding.alumnos.adapter = adapter
    }


    private fun studentsJSON(option:Int) {
        var urlJSON =""
        when (option) {
            1 -> urlJSON = "http://"+ipPuerto+"/estudiantesJSON"
            2 -> urlJSON = localtunnel+"/estudiantesJSON"
            else -> {
                Toast.makeText(this,getString(R.string.error2),Toast.LENGTH_SHORT).show()
            }
        }

        val jsonRequest = object : JsonArrayRequest(
            urlJSON,
            Response.Listener<JSONArray> { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    alumnosList.add(Alumno(estudiante.get("cuenta").toString(),estudiante.get("nombre").toString(),estudiante.get("edad").toString(), mutableListOf<Materia>()))
                }
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                 Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
        }

        volleyAPI.add(jsonRequest)
    }








}
