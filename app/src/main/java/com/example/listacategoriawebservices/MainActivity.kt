package com.example.listacategoriawebservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url ="https://firestore.googleapis.com/v1/projects/listacategoriaprueba/databases/(default)/documents/categorias"
    // Crear una cola de solicitudes
    lateinit var cola: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cola = Volley.newRequestQueue(this)

        pruebaAddCategoria("verano")
        pruebaGetCategorias()
        pruebaGetUnaCategoria()
        pruebaDeleteCategoria()
        pruebaActualizaUnaCategoria()

    }

    private fun pruebaAddCategoria(nombre: String) {
        val categoria = JSONObject()
        val fields = JSONObject()
        fields.put("nombre", JSONObject().put("stringValue", nombre))
        categoria.put("fields", fields)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, categoria,
            Response.Listener { response ->
                Log.d("Firestore", "Documento creado: $response")
            },
            Response.ErrorListener { error ->
                error.networkResponse?.let {
                    val errorData = String(it.data)
                    Log.e("Firestore", "Error: $errorData")
                }
            })

        cola.add(jsonObjectRequest)
    }

    private fun pruebaGetCategorias() {
        val solicitud = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Aquí asumimos que la respuesta contiene un objeto con una propiedad 'documents'
                val documents = response.getJSONArray("documents")
                for (i in 0 until documents.length()) {
                    val item = documents.getJSONObject(i)
                    val fields = item.getJSONObject("fields")
                    val nombre = fields.getJSONObject("nombre").getString("stringValue")
                    Log.d("Firestore", "Nombre: $nombre")

                }
            },
            Response.ErrorListener { error ->
                Log.e("Firestore", "Error: $error")
            })
        cola.add(solicitud)
    }

    private fun pruebaGetUnaCategoria() {
        val url = "https://firestore.googleapis.com/v1/projects/listacategoriaprueba/databases/(default)/documents/categorias/AhbEqtGEGsCtpzImvzyu"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("Firestore", "Documento obtenido: $response")
            },
            Response.ErrorListener { error ->
                Log.e("Firestore", "Error: $error")
            })

        cola.add(jsonObjectRequest)
    }

    private fun pruebaActualizaUnaCategoria() {
        TODO("Not yet implemented")
    }

    private fun pruebaDeleteCategoria() {
        val url = "https://firestore.googleapis.com/v1/projects/listacategoriaprueba/databases/(default)/documents/categorias/0GTGUZTAU5ucEv0Ib8zC"

        val solicitud = StringRequest(Request.Method.DELETE, url,
            Response.Listener { response ->
                Log.d("Firestore", "Documento eliminado")
            },
            Response.ErrorListener { error ->
                error.networkResponse?.let {
                    val errorData = String(it.data)
                    Log.e("Firestore", "Error: $errorData")
                }
            })

        cola.add(solicitud)
    }

}