package pe.com.grupo3.eventosulima.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pe.com.grupo3.eventosulima.Constantes
import pe.com.grupo3.eventosulima.R

class MainFragment : Fragment() {

    private lateinit var mUsername: TextView
    private lateinit var mrviListaPeliculas : RecyclerView
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
        mUsername = requireActivity().findViewById(R.id.main_title)
        val editor = requireActivity().getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)
        val username = editor.getString(Constantes.USERNAME, "")!!.uppercase()
        mUsername.text = "¡HOLA ${username}!"

        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
    }
}