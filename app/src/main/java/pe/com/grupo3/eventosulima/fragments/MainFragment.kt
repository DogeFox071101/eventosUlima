package pe.com.grupo3.eventosulima.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import pe.com.grupo3.eventosulima.Constantes
import pe.com.grupo3.eventosulima.R
import pe.com.grupo3.eventosulima.adapters.ListadoEventosMainAdapter
import pe.com.grupo3.eventosulima.adapters.ListadoPeliculasAdapter
import pe.com.grupo3.eventosulima.adapters.ListadoPeliculasMainAdapter
import pe.com.grupo3.eventosulima.models.GestorEventos
import pe.com.grupo3.eventosulima.models.GestorPeliculas
import pe.com.grupo3.eventosulima.models.beans.Evento
import pe.com.grupo3.eventosulima.models.beans.Pelicula

class MainFragment : Fragment() {

    private lateinit var mUsername: TextView
    private lateinit var mrviListaPeliculas : RecyclerView
    private lateinit var mrviListaEventos : RecyclerView
    private lateinit var toolbar : Toolbar

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
        mrviListaEventos = view.findViewById(R.id.rviListaEventos)
        val gestor = GestorPeliculas()
        val gestor1 = GestorEventos()
        val sp = requireActivity().getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)

        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            val estaSincronizado = sp.getBoolean(Constantes.SP_ESTA_SINCRONIZADO, false)
            var lista : List<Pelicula> = mutableListOf()
            val estaSincronizadoEventos = sp.getBoolean(Constantes.SP_ESTA_SINCRONIZADO_EVENTOS, false)
            var lista1 : List<Evento> = mutableListOf()


            var cont1 = 0
            var cont = 0

            gestor1.obtenerListaEventosFirebase({
                cont1 = it.size
            }){
                Toast.makeText(requireActivity(),
                    "Error: ${it}", Toast.LENGTH_SHORT).show()
            }

            gestor.obtenerListaPeliculasFirebase({
                cont = it.size
            }){
                Toast.makeText(requireActivity(),
                    "Error: ${it}", Toast.LENGTH_SHORT).show()
            }



            delay(1000)

            Log.i(null, "Es " + cont.toString())
            Log.i(null, "Eventos es " + cont1.toString())

            if(!estaSincronizado && cont == 0 && cont1 == 0) {
                lista = withContext(Dispatchers.IO) {
                    gestor.obtenerListaPeliculasCorrutinas()
                }

                lista1 = withContext(Dispatchers.IO) {
                    gestor1.obtenerListaEventosCorrutinas()
                }

                gestor.guardarListaPeliculasFirebase(lista, {
                    sp.edit().putBoolean(
                        Constantes.SP_ESTA_SINCRONIZADO, true).commit()
                    cargarListaPeliculasMain(lista)

                }){
                    Toast.makeText(requireActivity(),
                        "Error: ${it}", Toast.LENGTH_SHORT).show()
                }

                gestor1.guardarListaEventosFirebase(lista1, {
                    sp.edit().putBoolean(
                        Constantes.SP_ESTA_SINCRONIZADO_EVENTOS, true).commit()
                    cargarListaEventosMain(lista1)

                }){
                    Toast.makeText(requireActivity(),
                        "Error: ${it}", Toast.LENGTH_SHORT).show()
                }

            }else {
                Log.i(null, "Se ingresa aquí")
                gestor.obtenerListaPeliculasFirebase({
                    cargarListaPeliculasMain(it)
                }){
                    Toast.makeText(requireActivity(),
                        "Error: ${it}", Toast.LENGTH_SHORT).show()
                }

                gestor1.obtenerListaEventosFirebase({
                    cargarListaEventosMain(it)
                }){
                    Toast.makeText(requireActivity(),
                        "Error: ${it}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cargarListaPeliculasMain(lista : List<Pelicula>) {
        val adapter = ListadoPeliculasMainAdapter(lista)
        mrviListaPeliculas.adapter = adapter
    }

    private fun cargarListaEventosMain(lista : List<Evento>){
        val adapter = ListadoEventosMainAdapter(lista)
        mrviListaEventos.adapter = adapter
    }
}