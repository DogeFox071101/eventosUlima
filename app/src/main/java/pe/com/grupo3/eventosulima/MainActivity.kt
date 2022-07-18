package pe.com.grupo3.eventosulima

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import pe.com.grupo3.eventosulima.fragments.CarteleraFragment
import pe.com.grupo3.eventosulima.fragments.ListaEventosFragment
import pe.com.grupo3.eventosulima.fragments.MainFragment
import pe.com.grupo3.eventosulima.fragments.NosotrosFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mDlaMain : DrawerLayout
    private lateinit var mNviMain : NavigationView
    private lateinit var mToolbar : Toolbar
    private lateinit var mSettings : ImageView


    private val fragmentCartelera = CarteleraFragment()
    private val fragmentMain = MainFragment()
    private val fragmentNosotros = NosotrosFragment()
    private val fragmentListaEventos = ListaEventosFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mDlaMain = findViewById(R.id.mDlaMain)
        mNviMain = findViewById(R.id.nviListaOpciones)
        mToolbar = findViewById(R.id.toolbar)



        //setSupportActionBar(mToolbar)

        val sp = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE)
        val nameUser = sp.getString(Constantes.USERNAME, "")
        val header : View = mNviMain.getHeaderView(0)
        val eteNombre : TextView = header.findViewById(R.id.eteNombre)
        eteNombre.text = nameUser!!.uppercase()

        mNviMain.setNavigationItemSelectedListener {
            it.isChecked = true

            val ft = supportFragmentManager.beginTransaction()

            when(it.itemId) {
                R.id.menInicio -> mostrarFragmentMain(ft)
                R.id.menCartelera -> mostrarFragmentCartelera(ft)
                R.id.menSobreNosotros -> mostrarFragmentNosotros(ft)
                R.id.menOtrosEventos -> mostrarFragmentOtrosEventos(ft)
                R.id.menCerrarSesion -> cerrarSesion()
            }
            ft.addToBackStack(null)

            ft.commit()

            mDlaMain.closeDrawers()
            true
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcvEleccion, fragmentMain)
        ft.commit()

        mSettings = header.findViewById(R.id.config_starter)
        mSettings.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)



        MobileAds.initialize(this) {}
    }

    private fun mostrarFragmentOtrosEventos(ft: FragmentTransaction) {
        ft.replace(R.id.fcvEleccion, fragmentListaEventos)
    }

    private fun mostrarFragmentNosotros(ft: FragmentTransaction) {
        ft.replace(R.id.fcvEleccion, fragmentNosotros)
    }

    private fun cerrarSesion() {
        val editor = getSharedPreferences(Constantes.NOMBRE_SP, Context.MODE_PRIVATE).edit()
        editor.putBoolean(Constantes.SP_ESTA_LOGEADO, false)
        editor.putString(Constantes.USERNAME, "")
        editor.putString(Constantes.APELLIDOS, "")
        editor.putString(Constantes.NOMBRES, "")
        editor.putString(Constantes.CODIGO_ULIMA, "")
        editor.putString(Constantes.EDAD, "")
        editor.commit()
        finish()
    }

    private fun mostrarFragmentCartelera(ft: FragmentTransaction) {
        ft.replace(R.id.fcvEleccion, fragmentCartelera)
    }

    private fun mostrarFragmentMain(ft: FragmentTransaction) {
        ft.replace(R.id.fcvEleccion, fragmentMain)
    }

}