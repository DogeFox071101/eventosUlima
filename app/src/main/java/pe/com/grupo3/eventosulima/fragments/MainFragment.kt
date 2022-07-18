package pe.com.grupo3.eventosulima.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import pe.com.grupo3.eventosulima.Constantes
import pe.com.grupo3.eventosulima.R
import pe.com.grupo3.eventosulima.adapters.ListadoPeliculasAdapter
import pe.com.grupo3.eventosulima.adapters.ListadoPeliculasMainAdapter
import pe.com.grupo3.eventosulima.models.GestorPeliculas
import pe.com.grupo3.eventosulima.models.beans.Pelicula

class MainFragment : Fragment() {

    private lateinit var mUsername: TextView
    private lateinit var mrviListaPeliculas : RecyclerView
    private lateinit var toolbar : Toolbar
    private lateinit var iBtnMovies : ImageButton
    private lateinit var main_photo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "INICIO"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUsername = view.findViewById(R.id.main_title)
        val editor = requireActivity().getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)
        val username = editor.getString(Constantes.USERNAME, "")!!.uppercase()
        mUsername.text = "¡HOLA ${username}!"
        mrviListaPeliculas = view.findViewById(R.id.rviListaPeliculas)
        iBtnMovies = view.findViewById(R.id.iBtnMovies)
        val gestor = GestorPeliculas()
        val sp = requireActivity().getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)

        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        main_photo = requireActivity().findViewById(R.id.main_photo)

        GlobalScope.launch(Dispatchers.Main) {
            val estaSincronizado = sp.getBoolean(Constantes.SP_ESTA_SINCRONIZADO, false)
            var lista : List<Pelicula> = mutableListOf()

            var cont = 0

            gestor.obtenerListaPeliculasFirebase({
                cont = it.size
            }){
                Toast.makeText(requireActivity(),
                    "Error: $it", Toast.LENGTH_SHORT).show()
            }

            delay(1000)

            Log.i(null, "Es $cont")

            if(!estaSincronizado && cont == 0) {
                lista = withContext(Dispatchers.IO) {
                    gestor.obtenerListaPeliculasCorrutinas()
                }
                gestor.guardarListaPeliculasFirebase(lista, {
                    sp.edit().putBoolean(
                        Constantes.SP_ESTA_SINCRONIZADO, true).commit()
                    cargarListaPeliculasMain(lista)

                }){
                    Toast.makeText(requireActivity(),
                        "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }else {
                Log.i(null, "Se ingresa aquí")
                gestor.obtenerListaPeliculasFirebase({
                    cargarListaPeliculasMain(it)
                }){
                    Toast.makeText(requireActivity(),
                        "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
        main_photo.setOnClickListener {

        }

        iBtnMovies.setOnClickListener{
            val fragment = CarteleraFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fcvEleccion, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun cargarListaPeliculasMain(lista : List<Pelicula>) {
        val adapter = ListadoPeliculasMainAdapter(lista)
        mrviListaPeliculas.adapter = adapter
    }
}