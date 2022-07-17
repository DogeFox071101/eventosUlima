package pe.com.grupo3.eventosulima.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.com.grupo3.eventosulima.Constantes
import pe.com.grupo3.eventosulima.R
import pe.com.grupo3.eventosulima.adapters.ListadoPeliculasAdapter
import pe.com.grupo3.eventosulima.models.GestorPeliculas
import pe.com.grupo3.eventosulima.models.beans.Pelicula
import java.util.concurrent.RecursiveAction

class CarteleraFragment : Fragment() {

    private lateinit var mRviPeliculas : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "CARTELERA"
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cartelera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRviPeliculas = requireActivity().findViewById(R.id.rviCartelera)

        val gestor = GestorPeliculas()
        val sp = requireActivity().getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)

        GlobalScope.launch(Dispatchers.Main) {
            val estaSincronizado = sp.getBoolean(Constantes.SP_ESTA_SINCRONIZADO, false)
            var lista : List<Pelicula> = mutableListOf()
            if(!estaSincronizado) {
                lista = withContext(Dispatchers.IO) {
                    gestor.obtenerListaPeliculasCorrutinas()
                }
                gestor.guardarListaPeliculasFirebase(lista, {
                    sp.edit().putBoolean(
                        Constantes.SP_ESTA_SINCRONIZADO, true).commit()
                    cargarListaPeliculas(lista)

                }){
                    Toast.makeText(requireActivity(),
                    "Error: ${it}", Toast.LENGTH_SHORT).show()
                }
            }else {
                Log.i(null, "Se ingresa aquí")
                gestor.obtenerListaPeliculasFirebase({
                    cargarListaPeliculas(it)
                }){
                    Toast.makeText(requireActivity(),
                    "Error: ${it}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun cargarListaPeliculas(lista: List<Pelicula>) {
        val adapter = ListadoPeliculasAdapter(lista){
            Toast.makeText(requireActivity(),
                "Seleccionaste: ${it.titulo}", Toast.LENGTH_SHORT).show()
            val argumentos = Bundle()

            argumentos.putString("tituloPelicula", it.titulo)
            argumentos.putString("urlImagenPelicula", it.urlImagen)
            argumentos.putString("directorPelicula", it.director)
            argumentos.putString("actoresPelicula", it.actores)
            argumentos.putString("fechaPelicula", it.fecha)
            argumentos.putString("idiomaPelicula", it.idioma)
            argumentos.putString("paisPelicula", it.pais)
            argumentos.putString("duracionPelicula", it.duracion)
            argumentos.putString("generoPelicula", it.genero)
            argumentos.putString("diaFuncionPelicula", it.diaFuncion)
            argumentos.putString("horaInicioPelicula", it.horaInicio)

            val peliculaFragment = PeliculaFragment()
            peliculaFragment.arguments = argumentos

            val ft = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fcvEleccion, peliculaFragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        mRviPeliculas.adapter = adapter
    }

}